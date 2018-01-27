package com.kloudtek.unpack;

import com.kloudtek.unpack.transformer.SetPropertyTransformer;
import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;
import com.kloudtek.util.UnexpectedException;
import com.kloudtek.util.io.IOUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.kloudtek.unpack.FileType.DIR;
import static com.kloudtek.unpack.FileType.ZIP;
import static com.kloudtek.unpack.UFile.generatePath;
import static org.junit.jupiter.api.Assertions.*;

class UnpackerTest {
    public static final String PATH_TEXTFILE = "textfile.txt";
    public static final String FOO = "foo";
    public static final String SOMEDIR = "somedir";
    public static final String SOMEFILE = "somefile";
    public static final String BAR = "bar";
    public static final String TEST_PROPERTIES = "test.properties";
    public static final String GAH = "gah";
    public static final String BAZ = "baz";
    private static File tmpDir;
    private File srcFile;
    private File dstFile;
    private SourceBuilder builder;
    private Verifier verifier;
    private Unpacker unpacker;

    @BeforeAll
    public static void createTempDir() throws IOException {
        tmpDir = new File("target");
        if (!tmpDir.exists()) {
            tmpDir = new File("lib" + File.separator + "target");
            if (!tmpDir.exists()) {
                tmpDir = new File(".");
            }
        }
        tmpDir = new File(tmpDir, "_teststmp");
        if (!tmpDir.exists() & !tmpDir.mkdirs()) {
            throw new IOException("Unable to create temp dir: " + tmpDir.getAbsolutePath());
        }
    }

    @AfterAll
    public static void deleteTempDir() throws IOException {
        FileUtils.delete(tmpDir);
    }

    @AfterEach
    public void cleanupAfterTest() throws IOException {
        FileUtils.delete(srcFile, srcFile);
    }

    @ParameterizedTest
    @MethodSource("srcAndDestTypes")
    public void simpleCopyFiles(FileType srcType, FileType dstType) throws Exception {
        init(srcType, dstType);
        builder.addText(PATH_TEXTFILE, FOO);
        builder.mkdir(SOMEDIR).addText(SOMEFILE, BAR);
        builder.build();
        unpacker.unpack();
        verifier.exists();
        verifier.textFile(PATH_TEXTFILE, FOO);
        verifier.dir(SOMEDIR).textFile(SOMEFILE, BAR);
    }

    @Test
    public void testSetPropertyNewFile() throws Exception {
        init(DIR, DIR);
        unpacker.addTransformer(new SetPropertyTransformer(TEST_PROPERTIES, FOO, BAR));
        builder.build();
        unpacker.unpack();
        verifier.properties(TEST_PROPERTIES, FOO, BAR);
    }

    @Test
    public void testSetPropertyExistingFile() throws Exception {
        init(DIR, DIR);
        unpacker.addTransformer(new SetPropertyTransformer(TEST_PROPERTIES, FOO, BAR));
        builder.properties(TEST_PROPERTIES, GAH, BAZ);
        builder.build();
        unpacker.unpack();
        verifier.properties(TEST_PROPERTIES, FOO, BAR, GAH, BAZ);
    }

    static Stream<Arguments> srcAndDestTypes() {
        return Stream.of(
                Arguments.of(DIR, DIR),
                Arguments.of(ZIP, DIR),
                Arguments.of(DIR, ZIP),
                Arguments.of(ZIP, ZIP)
        );
    }

    private void init(FileType src, FileType dest) throws UnpackException {
        try {
            String testId = UUID.randomUUID().toString();
            System.out.println("Test ID: " + testId+" SRC: "+src+" DST: "+dest);
            srcFile = new File(tmpDir, testId + "-src." + src.getExtension());
            dstFile = new File(tmpDir, testId + "-dst." + src.getExtension());
            builder = createSource(src);
            verifier = createVerifier(dest);
            unpacker = new Unpacker(srcFile, src, dstFile, dest);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

    private SourceBuilder createSource(FileType type) throws IOException {
        switch (type) {
            case DIR:
                return new FileBuilder(srcFile);
            case ZIP:
                return new ZipBuilder(srcFile);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    private Verifier createVerifier(FileType type) throws IOException {
        switch (type) {
            case DIR:
                return new FileVerifier(dstFile);
            case ZIP:
                return new ZipVerifier(dstFile);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    abstract class SourceBuilder {
        public SourceBuilder addText(String path, String text) throws IOException {
            return addData(path, StringUtils.utf8(text));
        }

        public abstract SourceBuilder addData(String path, byte[] data) throws IOException;

        public void build() throws IOException {
        }

        public abstract SourceBuilder mkdir(String name);

        public void properties(String path, String key, String value) {
            try {
                Properties p = new Properties();
                p.setProperty(key,value);
                addData(path,IOUtils.toByteArray(os -> p.store(os,"")));
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    class FileBuilder extends SourceBuilder {
        private File file;

        public FileBuilder(File file) throws IOException {
            this.file = file;
            if (!file.exists() && !file.mkdirs()) {
                throw new IOException("Unable to create dir: " + file.getPath());
            }
        }

        @Override
        public SourceBuilder addData(String path, byte[] data) throws IOException {
            File file = new File(this.file + File.separator + path.replace("/", File.separator));
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                FileUtils.mkdirs(parentFile);
            }
            FileUtils.write(file, data);
            return this;
        }

        @Override
        public SourceBuilder mkdir(String name) {
            try {
                File dir = new File(this.file, name);
                return new FileBuilder(dir);
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    class ZipBuilder extends SourceBuilder {
        private final ZipArchiveOutputStream os;
        private String path;

        public ZipBuilder(File file) throws IOException {
            os = new ZipArchiveOutputStream(file);
        }

        public ZipBuilder(ZipArchiveOutputStream os, String path) throws IOException {
            this.os = os;
            this.path = path;
        }

        @Override
        public SourceBuilder addData(String path, byte[] data) throws IOException {
            ZipArchiveEntry archiveEntry = new ZipArchiveEntry(this.path == null ? path : this.path + "/" + path);
            archiveEntry.setSize(data.length);
            os.putArchiveEntry(archiveEntry);
            os.write(data);
            os.closeArchiveEntry();
            return this;
        }

        @Override
        public void build() throws IOException {
            os.close();
        }

        @Override
        public SourceBuilder mkdir(String name) {
            try {
                return new ZipBuilder(os, path == null ? name : this.path + "/" + name);
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    abstract class Verifier {
        Verifier textFile(String path, String expected) {
            String content = StringUtils.utf8(getData(path));
            assertEquals(expected, content);
            return this;
        }

        public abstract byte[] getData(String path);

        public abstract void exists();

        public abstract Verifier dir(String dir);

        public Verifier properties(String path, String key, String value) {
            Properties p = getProperties(path);
            assertEquals(1,p.size());
            assertTrue(p.containsKey(key));
            assertEquals(value,p.get(key));
            return this;
        }

        public Verifier properties(String path, String key1, String value1, String key2, String value2) {
            Properties p = getProperties(path);
            assertEquals(2,p.size());
            assertTrue(p.containsKey(key1));
            assertEquals(value1,p.get(key1));
            assertTrue(p.containsKey(key2));
            assertEquals(value2,p.get(key2));
            return this;
        }

        private Properties getProperties(String path) {
            try {
                Properties p = new Properties();
                byte[] data = getData(path);
                p.load(new ByteArrayInputStream(data));
                return p;
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }
    }

    class FileVerifier extends Verifier {
        private File file;

        public FileVerifier(File file) {
            this.file = file;
        }

        public void exists() {
            assertTrue(file.exists(), "File doesn't exist: " + file.getPath());
        }

        @Override
        public byte[] getData(String path) {
            try {
                File dataFile = new File(this.file + File.separator + path.replace("/", File.separator));
                assertFileExists(dataFile);
                return IOUtils.toByteArray(dataFile);
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }

        @Override
        public Verifier dir(String dir) {
            FileVerifier verifier = new FileVerifier(new File(file, dir));
            verifier.exists();
            return verifier;
        }
    }

    class ZipVerifier extends Verifier {
        private File file;
        private ZipFile zipFile;
        private String path;

        public ZipVerifier(File file) {
            this.file = file;
        }

        public ZipVerifier(ZipFile zipFile, String path) {
            this.zipFile = zipFile;
            this.path = path;
        }

        @Override
        public byte[] getData(String path) {
            try {
                ZipEntry entry = zipFile.getEntry(generatePath(this.path, path));
                try (InputStream is = zipFile.getInputStream(entry)) {
                    return IOUtils.toByteArray(is);
                }
            } catch (IOException e) {
                throw new AssertionFailedError(e.getMessage(), e);
            }
        }

        @Override
        public void exists() {
            try {
                assertFileExists(file);
                zipFile = new ZipFile(file);
            } catch (IOException e) {
                Assertions.fail(e);
            }
        }

        @Override
        public Verifier dir(String path) {
            return new ZipVerifier(zipFile, generatePath(this.path, path));
        }
    }

    private static void assertFileExists(File dataFile) {
        assertTrue(dataFile.exists(), "File " + dataFile.getAbsolutePath() + " does not exist");
    }

    private static void assertIsDir(File file) {
        assertIsDir(file, null);
    }

    private static void assertIsDir(File file, Integer childCount) {
        assertFileExists(file);
        assertTrue(file.isDirectory(), "File is not a directory: " + file.getAbsolutePath());
        if (childCount != null) {
            File[] childrens = file.listFiles();
            assertNotNull(childrens, "Directory doesn't contain any files but " + childCount + " were expected: " + file.getAbsolutePath());
            assertEquals(childCount.intValue(), childrens.length, "Directory has " + childrens.length + " files but " + childCount + " were expected: " + file.getAbsolutePath());
        }
    }
}

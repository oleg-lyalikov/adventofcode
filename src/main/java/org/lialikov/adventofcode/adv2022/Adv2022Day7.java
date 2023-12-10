package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2022Day7 {

    public static void main(String[] args) {
        System.out.println(getDirSum("2022/2022-12-07-sample.txt"));
        System.out.println(getDirSum("2022/2022-12-07.txt"));
    }

    private static final Pattern CD = Pattern.compile("\\$\\s+cd\\s+(.*)");
    private static final Pattern LS = Pattern.compile("\\$\\s+ls\\s*");
    private static final Pattern LIST_DIR = Pattern.compile("dir\\s+(.*)");
    private static final Pattern LIST_FILE = Pattern.compile("(\\d+)\\s+(.*)");

    private static Pair<Long, Long> getDirSum(String f) {
        final File ROOT = new File("/", null, true);
        final AtomicReference<File> currentDir = new AtomicReference<>();
        FileUtil.read(f, l -> {
            Matcher m = CD.matcher(l);
            if (m.matches()) {
                String dir = m.group(1);
                if ("/".equals(dir)) {
                    currentDir.set(ROOT);
                } else if ("..".equals(dir)) {
                    currentDir.set(currentDir.get().parent);
                } else {
                    currentDir.set(currentDir.get().children.stream()
                            .filter(file -> file.isDir && file.name.equals(dir))
                            .findAny()
                            .orElseThrow(() -> new IllegalStateException("Cannot find dir "+ dir))
                    );
                }
                return;
            }
            m = LS.matcher(l);
            if (m.matches()) {
                return;
            }
            m = LIST_DIR.matcher(l);
            if (m.matches()) {
                File dir = new File(m.group(1), currentDir.get(), true);
                currentDir.get().addChildren(dir);
                return;
            }
            m = LIST_FILE.matcher(l);
            if (m.matches()) {
                long size = Long.parseLong(m.group(1));
                String name = m.group(2);
                File file = new File(name, currentDir.get(), false);
                file.setSize(size);
                currentDir.get().addChildren(file);
            }
        });

        calcSizes(ROOT);

        final AtomicLong firstRes = new AtomicLong(0);
        iterate(ROOT, file -> {
            if (file.isDir && file.size <= 100000L) {
                firstRes.addAndGet(file.size);
            }
        });

        final AtomicLong secondRes = new AtomicLong(0);
        final long unusedSpace = 70000000L - ROOT.size;
        final long toFree = 30000000 - unusedSpace;
        List<File> dirs = new ArrayList<>();
        iterate(ROOT, file -> {
            if (file.isDir) {
                dirs.add(file);
            }
        });
        dirs.sort(Comparator.comparingLong(o -> o.size));
        for (File dir : dirs) {
            if (dir.size >= toFree) {
                secondRes.set(dir.size);
                break;
            }
        }

        return Pair.of(firstRes.get(), secondRes.get());
    }

    private static long calcSizes(File file) {
        if (!file.isDir || file.sizeSet) {
            return file.size;
        }

        long res = 0;
        for (File child : file.children) {
            res += calcSizes(child);
        }
        file.setSize(res);
        return res;
    }

    private static void iterate(File file, Consumer<File> consumer) {
        for (File child : file.children) {
            iterate(child, consumer);
        }
        consumer.accept(file);
    }

    public static class File {
        private final String name;
        private final File parent;
        private final boolean root;
        private final boolean isDir;
        private long size;
        private boolean sizeSet = false;
        private final List<File> children = new ArrayList<>();

        public File(String name, File parent, boolean isDir) {
            this(name, parent, parent == null, isDir);
        }

        private File(String name, File parent, boolean root, boolean isDir) {
            this.name = name;
            this.parent = parent;
            this.root = root;
            this.isDir = isDir;
        }

        public void setSize(long size) {
            this.size = size;
            this.sizeSet = true;
        }

        public void addChildren(File child) {
            Optional<File> existing = children.stream()
                    .filter(c -> c.name.equals(child.name))
                    .findAny();
            if (existing.isPresent()) {
                throw new IllegalStateException("Already exists child " + child.name);
            }
            children.add(child);
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + name + '\'' +
                    ", parent=" + parent +
                    ", root=" + root +
                    ", isDir=" + isDir +
                    ", size=" + size +
                    '}';
        }
    }

}

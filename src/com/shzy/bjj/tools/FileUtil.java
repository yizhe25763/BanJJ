package com.shzy.bjj.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static final char SEP = '/';
    static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    static volatile int fileIndex = 0;

    public FileUtil() {
    }

    public static boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith("jpg") || name.endsWith("bmp") || name.endsWith("png") || name.endsWith("gif");
    }

    public static File createTemp(File dir, String ext) {
        if(!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, format.format(new Date()) + fileIndex++ + "." + ext);
    }

    public static File[] listFiles(File file) {
        return file == null?new File[0]:file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });
    }

    public static File[] listDirectories(File file) {
        return file == null?new File[0]:file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    public static boolean isValid(File file) {
        return file != null && file.exists() && file.length() > 0L;
    }

    public static final String concat(String path, String separator, String file) {
        StringBuilder sb = new StringBuilder(path.length() + separator.length() + file.length());
        sb.append(path).append(separator);
        sb.append(file);
        return sb.toString();
    }

    public static final String concat(String path, char sep, String file) {
        StringBuilder sb = new StringBuilder(path.length() + file.length() + 1);
        sb.append(path).append(sep);
        sb.append(file);
        return sb.toString();
    }

    public static final String getExtFromName(File file) {
        return getExtFromName((String)file.getName());
    }

    public static final String getExtFromName(String fileName) {
        if(fileName == null) {
            return null;
        } else {
            int pos = fileName.lastIndexOf(".");
            if(pos < 0) {
                return null;
            } else {
                String ext = fileName.substring(pos + 1);
                pos = ext.indexOf("?");
                if(pos >= 0) {
                    ext = ext.substring(0, pos);
                }

                return ext.length() == 0?null:ext;
            }
        }
    }

    public static final String getPrefixFromName(String fileName) {
        if(fileName == null) {
            return null;
        } else {
            int pos = fileName.lastIndexOf(".");
            return pos < 0?fileName:fileName.substring(0, pos);
        }
    }

    public static final String getPrefixFromName(File file) {
        return getPrefixFromName((String)file.getName());
    }

    public static final boolean delete(File file) {
        if(file == null) {
            return true;
        } else if(!file.exists()) {
            return true;
        } else if(file.isFile()) {
            return file.delete();
        } else {
            boolean del = true;
            File[] files = file.listFiles();
            if(files == null) {
                return file.delete();
            } else {
                for(int i = 0; i < files.length; ++i) {
                    del = delete(files[i]);
                    if(!del) {
                        break;
                    }
                }

                return del?file.delete():del;
            }
        }
    }

    public static final boolean clear(File path) {
        if(path == null) {
            return false;
        } else if(!path.exists()) {
            return false;
        } else if(path.isFile()) {
            return false;
        } else {
            boolean del = false;
            File[] files = path.listFiles();
            if(files == null) {
                return true;
            } else {
                for(int i = 0; i < files.length; ++i) {
                    del = delete(files[i]);
                    if(!del) {
                        break;
                    }
                }

                return del;
            }
        }
    }

    public static final boolean create(File file) {
        File parent = getParentFile(file);
        return parent == null?false:(!parent.exists()?parent.mkdirs():parent.isDirectory() && parent.canRead() && parent.canWrite());
    }

    public static File getParentFile(File file) {
        File parent = file.getParentFile();
        if(parent == null) {
            try {
                parent = file.getCanonicalFile().getParentFile();
            } catch (IOException var3) {
                return null;
            }
        }

        return parent;
    }

    public static final boolean renameTo(File src, File dest) {
        create((File)dest);
        return !src.renameTo(dest)?(dest.exists() && dest.delete()?src.renameTo(dest):false):true;
    }

    public static final File create(String file) {
        File f = new File(file);
        return create((File)f)?f:null;
    }

    public static String catPath(String lookupPath, String path) {
        int index = lookupPath.lastIndexOf("/");

        for(lookupPath = lookupPath.substring(0, index); path.startsWith("../"); path = path.substring(index)) {
            if(lookupPath.length() <= 0) {
                return null;
            }

            index = lookupPath.lastIndexOf("/");
            lookupPath = lookupPath.substring(0, index);
            index = path.indexOf("../") + 3;
        }

        if(path.startsWith("/")) {
            return path;
        } else if(lookupPath.endsWith("/")) {
            return lookupPath + path;
        } else {
            return lookupPath + "/" + path;
        }
    }

    public static String safePath(String base, String path) {
        String normP = path;
        if(path.indexOf(92) >= 0) {
            normP = path.replace('\\', '/');
        }

        if(!normP.startsWith("/")) {
            normP = "/" + normP;
        }

        int index = normP.indexOf("/../");
        if(index >= 0) {
            int realPath = 0;

            while((realPath = normP.indexOf("//", realPath)) >= 0) {
                normP = normP.substring(0, realPath) + normP.substring(realPath + 1);
                if(realPath < index) {
                    --index;
                }
            }

            realPath = 0;

            while((realPath = normP.indexOf("/./", realPath)) >= 0) {
                normP = normP.substring(0, realPath) + normP.substring(realPath + 2);
                if(realPath < index) {
                    index -= 2;
                }
            }

            while(index >= 0) {
                if(index == 0) {
                    return null;
                }

                realPath = normP.lastIndexOf(47, index - 1);
                normP = normP.substring(0, realPath) + normP.substring(index + 3);
                index = normP.indexOf("/../", realPath);
            }
        }

        String var8 = base + normP;
        var8 = patch(var8);
        String canPath = null;

        try {
            canPath = (new File(var8)).getCanonicalPath();
        } catch (IOException var7) {
            return null;
        }

        if(File.separatorChar == 92 && !var8.equals(canPath)) {
            int ls = var8.lastIndexOf(92);
            if(ls > 0 && !var8.substring(0, ls).equals(canPath)) {
                return null;
            }
        }

        return var8.indexOf("..") != -1?null:var8;
    }

    public static String patch(String path) {
        String patchPath = path.trim();
        if(patchPath.length() >= 3 && patchPath.charAt(0) == 47 && Character.isLetter(patchPath.charAt(1)) && patchPath.charAt(2) == 58) {
            patchPath = patchPath.substring(1, 3) + "/" + patchPath.substring(3);
        }

        if(patchPath.length() >= 2 && Character.isLetter(patchPath.charAt(0)) && patchPath.charAt(1) == 58) {
            char[] ca = patchPath.replace('/', '\\').toCharArray();
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < ca.length; ++i) {
                if(ca[i] != 92 || ca[i] == 92 && i > 0 && ca[i - 1] != 92) {
                    char c;
                    if(i == 0 && Character.isLetter(ca[i]) && i < ca.length - 1 && ca[i + 1] == 58) {
                        c = Character.toUpperCase(ca[i]);
                    } else {
                        c = ca[i];
                    }

                    sb.append(c);
                }
            }

            patchPath = sb.toString();
        }

        return patchPath;
    }

    public static boolean isAbsolute(String path) {
        return path.startsWith("/")?true:(path.startsWith(File.separator)?true:path.length() >= 3 && Character.isLetter(path.charAt(0)) && path.charAt(1) == 58);
    }

    public static String getCanonicalPath(String name) {
        if(name == null) {
            return null;
        } else {
            File f = new File(name);

            try {
                return f.getCanonicalPath();
            } catch (IOException var3) {
                return name;
            }
        }
    }

    private static boolean isSep(char c) {
        return c == 47 || c == 92;
    }

    public static final String normalize(String path) {
        if(path == null) {
            return null;
        } else {
            int len = path.length();
            StringBuilder sb = new StringBuilder(len);
            boolean changed = false;
            int pos = 0;
            boolean c = false;

            while(pos < len) {
                char var7 = path.charAt(pos);
                if(isSep(var7)) {
                    while(pos + 1 < len && isSep(path.charAt(pos + 1))) {
                        ++pos;
                        changed = true;
                    }

                    if(pos + 1 < len && path.charAt(pos + 1) == 46) {
                        if(pos + 2 >= len) {
                            break;
                        }

                        switch(path.charAt(pos + 2)) {
                        case '.':
                            if(pos + 3 < len && isSep(path.charAt(pos + 3))) {
                                pos += 3;

                                int separatorPos;
                                for(separatorPos = sb.length() - 1; separatorPos >= 0 && !isSep(sb.charAt(separatorPos)); --separatorPos) {
                                    ;
                                }

                                if(separatorPos >= 0) {
                                    sb.setLength(separatorPos);
                                    changed = true;
                                }
                                continue;
                            }
                            break;
                        case '/':
                        case '\\':
                            pos += 2;
                            continue;
                        }
                    }
                }

                sb.append(var7);
                ++pos;
            }

            return changed?sb.toString():path;
        }
    }
}

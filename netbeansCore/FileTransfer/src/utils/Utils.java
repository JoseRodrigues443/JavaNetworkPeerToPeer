/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.scene.control.ComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class Utils {

    public final static String ZIPPED_FILE_NAME_DEFAULT = ".zipped.file.java.nao.mexer.rcomp.temporario.dropbox.fanado.zip";
    private final static String REGEX_PUBLIC_IP_ADDRESS_DEFAULT = "^([0-9]|"
            + "[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(?<!172\\.(16|17|18|19"
            + "|20|21|22|23|24|25|26|27|28|29|30|31))(?<!127)(?<!^10)(?<!^0)\\."
            + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(?<!192\\.168)(?<"
            + "!172\\.(16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31))\\.([0-9]|"
            + "[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.([0-9]|[1-9][0-9]|1[0-9]"
            + "{2}|2[0-4][0-9]|25[0-5])(?<!\\.0$)(?<!\\.255$)$";

    /**
     * It transforms seconds to milisconds, if the value inserted isnt bigger
     * than 0, then it will use the default value, that is final and always
     * bigger than 0
     *
     * @param seconds
     * @param defaultValue
     * @return
     */
    public static int secondsToMiliseconds(int seconds, int defaultValue) {
        return (seconds > 0 ? seconds * 1000 : defaultValue * 1000);
    }

    /**
     * in a list only returns the ones that are "ivp4" ou "ipv6" protocol, as
     * asked
     *
     * "mask" or "address" or "broadcast"
     *
     *
     *
     * @param ipv4Ipv6
     * @param maskOrAddress
     * @param ipList
     * @return
     */
    private static String cleanIpString(String ipv4Ipv6, String maskOrAddress, ArrayList<String> ipList) {
        for (String s : ipList) {
            if (ipv4Ipv6.equalsIgnoreCase("ipv4")) {
                if (maskOrAddress.equalsIgnoreCase("mask")) {
                    if (s.matches("\\/[1-3]?[0-9]")) {
                        /*validate cases like: "/31", "/9" etc*/
                        return s;

                    }
                } else if (maskOrAddress.equalsIgnoreCase("address")) {
                    s = s.trim();
                    s = s.replaceAll("/", "");
                    if (validateAddress(s, "ipv4")) {
                        /**
                         * ignore loopback address
                         */
                        if (!s.matches(REGEX_PUBLIC_IP_ADDRESS_DEFAULT) && !s.contains("127.0.0.1")) {
                            return s;
                        }
                    }
                } else if (maskOrAddress.equalsIgnoreCase("broadcast")) {
                    /**
                     * if is broadcast address
                     */
                    if (s.charAt(0) == '/') {
                        StringBuilder tmp = new StringBuilder(s);
                        tmp.deleteCharAt(0);
                        s = tmp.toString();
                    }
                    if (validateAddress(s, "ipv4")
                            && !s.equalsIgnoreCase("127.255.255.255")
                            && !s.equalsIgnoreCase("192.255.255.255")) {
                        /*ALTERA PORRA*/
                        return s;
                    }
                }
            } else if (ipv4Ipv6.equalsIgnoreCase("ipv6")) {
                /*not implemented*/
            }
        }
        return "172.0.0.2";
        /*default, never local host*/
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    /**
     * Used in the client to discover de tcp port
     *
     * @param ipv4
     * @return
     */
    public static int sumOfAllIpv4Digits(String ipv4) {
        int sum = 0;
        if (!validateAddress(ipv4, "ipv4")) {
            /*if validate ipv4 address FAILED*/
            return ipv4.length() + 1;
        }
        Integer number;
        String[] splitedIp = ipv4.split("\\.");
        /*split by dot ".", because split uses regex and dot is a speciel  element, it needs the \\*/
        for (String s : splitedIp) {
            /*is numeric*/
            if (s.matches("[0-9]*")) {
                number = new Integer(s);
                sum += number;
            }
        }
        return (sum > 0) ? sum : ipv4.length() + 1;
    }

    /**
     * "ipv4" OR "ipv6"
     *
     * @param ipv4Ipv6
     * @return
     */
    public static String getMachineIpAddress(String ipv4Ipv6) {
        ArrayList<String> ipList = new ArrayList<>();
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    ipList.add(i.getHostAddress());
                }
            }
        } catch (SocketException e) {
            System.out.println();
            utils.Utils.print("--> SOCKET ERROR EXCEPTION: " + e.toString());
        }

        return cleanIpString(ipv4Ipv6, "address", ipList);
    }

    public static String getMachineBroadcastAddressIpv4() {
        return returnMachineBroadcastOrSubnetMaskAddress("ipv4", "broadcast");
    }

    public static String getMachineMaskAddressIpv4() {
        return returnMachineBroadcastOrSubnetMaskAddress("ipv4", "mask");
    }

    /**
     * "ipv4" OR "ipv6"
     *
     * "broadcast" OR "mask"
     *
     * @param ipv4Ipv6
     * @return
     */
    private static String returnMachineBroadcastOrSubnetMaskAddress(String ipv4Ipv6, String BroadcastOrSubnetMask) {
        ArrayList<String> ipList = new ArrayList<>();
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                List<InterfaceAddress> ee = n.getInterfaceAddresses();
                Iterator<InterfaceAddress> it = ee.iterator();
                while (it.hasNext()) {
                    //InetAddress i = (InetAddress) ee.nextElement();
                    InterfaceAddress ia = it.next();
                    if (BroadcastOrSubnetMask.equalsIgnoreCase("broadcast")) {
                        InetAddress inet = ia.getBroadcast();
                        if (inet != null) {
                            ipList.add(ia.getBroadcast().toString());
                        }
                    } else if (BroadcastOrSubnetMask.equalsIgnoreCase("mask")) {
                        ipList.add("/" + ia.getNetworkPrefixLength());
                    }
                }
            }
        } catch (SocketException e) {
            //System.out.println("SOCKET ERROR EXCEPTION: " + e.toString());
            utils.Utils.print("SOCKET ERROR EXCEPTION: " + e.toString());
        }

        return (BroadcastOrSubnetMask.equalsIgnoreCase("broadcast"))
                ? cleanIpString(ipv4Ipv6, BroadcastOrSubnetMask, ipList)
                : cleanIpString(ipv4Ipv6, "mask", ipList);
    }

    /**
     *
     * ADDRESS TYPE --> addressType: "ipv4" OR "ipv6"
     *
     * @param address
     * @param addressType
     * @return
     */
    public static boolean validateAddress(String address, String addressType) {
        boolean toReturn = false;
        String regex = "";
        if (addressType.equalsIgnoreCase("ipv4")) {
            /*IPV4*/
            regex
                    = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
            /*regex origin: https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/*/
        } else if (addressType.equalsIgnoreCase("ipv6")) {
            /*IPV6*/
            regex = "(\\A([0-9a-f]{1,4}:){1,1}(:[0-9a-f]{1,4}){1,6}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,2}(:[0-9a-f]{1,4}){1,5}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,3}(:[0-9a-f]{1,4}){1,4}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,4}(:[0-9a-f]{1,4}){1,3}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,5}(:[0-9a-f]{1,4}){1,2}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,6}(:[0-9a-f]{1,4}){1,1}\\Z)|"
                    + "(\\A(([0-9a-f]{1,4}:){1,7}|:):\\Z)|"
                    + "(\\A:(:[0-9a-f]{1,4}){1,7}\\Z)|"
                    + "(\\A((([0-9a-f]{1,4}:){6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})\\Z)|"
                    + "(\\A(([0-9a-f]{1,4}:){5}[0-9a-f]{1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3})\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){5}:[0-9a-f]{1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,1}(:[0-9a-f]{1,4}){1,4}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,2}(:[0-9a-f]{1,4}){1,3}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,3}(:[0-9a-f]{1,4}){1,2}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A([0-9a-f]{1,4}:){1,4}(:[0-9a-f]{1,4}){1,1}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A(([0-9a-f]{1,4}:){1,5}|:):(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)|"
                    + "(\\A:(:[0-9a-f]{1,4}){1,5}:(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\Z)";
            /*REGEX origin: http://vernon.mauery.com/content/projects/linux/ipv6_regex*/
        } else {
            return false;
        }
        return address.matches(regex);
    }

    public static String totalSizeOfFiles(ArrayList<File> files) {
        Long l = 0l;
        for (File f : files) {
            l += f.length();
        }
        return bytesToHumanFormat(l);
    }

    public static String fileBytesOfFile(File files) {
        Long l = 0l;
        l += files.length();

        return floatForm(l);
    }

    private static String bytesToHumanFormat(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) {
            return floatForm(size) + " byte";
        }
        if (size >= Kb && size < Mb) {
            return floatForm((double) size / Kb) + " Kb";
        }
        if (size >= Mb && size < Gb) {
            return floatForm((double) size / Mb) + " Mb";
        }
        if (size >= Gb && size < Tb) {
            return floatForm((double) size / Gb) + " Gb";
        }
        if (size >= Tb && size < Pb) {
            return floatForm((double) size / Tb) + " Tb";
        }
        if (size >= Pb && size < Eb) {
            return floatForm((double) size / Pb) + " Pb";
        }
        if (size >= Eb) {
            return floatForm((double) size / Eb) + " Eb";
        }

        return "ERRO";
    }

    public static DefaultListModel returnModelForJList(ArrayList<File> files) {
        DefaultListModel model = new DefaultListModel<>();
        for (File f : files) {
            model.addElement(f.getName());
        }

        return model;
    }

    public static DefaultListModel returnModelForJListStringFormat(ArrayList<String> string) {
        DefaultListModel model = new DefaultListModel<>();
        for (String s : string) {
            model.addElement(s);
        }

        return model;
    }

    public static ArrayList<String> listAllFilesStringList(ArrayList<File> listAllFiles) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (File f : listAllFiles) {
            toReturn.add(f.getName());
        }
        if (toReturn.isEmpty()) {
            toReturn.add("Empty directory");
        }
        return toReturn;
    }

    /**
     *
     * @param path
     * @param isFileOrDirectory true = file, false = directory
     * @param createMissing true = create missing directorys/files false = just
     * returns false
     * @return
     */
    public static boolean validadeFileCreateIfMissing(String path, boolean isFileOrDirectory, boolean createMissing) {
        File f = new File(path);
        boolean toReturn = false;

        /**
         * If does not exist
         */
        if (!f.exists()) {
            //System.out.println();
            print("--> Diretorio nao existe, a criar-lo\n\nSource: " + path);
            /**
             * creates directory files
             *
             * ex: "./etc/apt/souce.list" will create all directorys
             *
             */

            /**
             * nao é pasta
             */
            if (isFileOrDirectory) {
                //System.out.println();

                if (createMissing) {
                    print("--> A criar pasta para ficheiro");
                    f.getParentFile().mkdirs();
                    toReturn = true;
                }

            } else if (!isFileOrDirectory) {
                /**
                 * is directory
                 */
                if (createMissing) {
                    print("--> A criar pasta");
                    f.mkdirs();
                    toReturn = true;
                }

            } else {
                //System.out.println();
                print("--> Estranho, isto não é pasta nem ficheiro");
            }
        } else {
            return true;
        }
        return toReturn;
    }

    /**
     * com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
     * com.sun.java.swing.plaf.motif.MotifLookAndFeel;
     * com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
     * com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
     *
     *
     * TYPE: POSSIBILITIES
     *
     * gtk
     *
     * motif
     *
     * windows
     *
     * nimbus
     *
     * autodetect //this will automatic change by OS
     *
     *
     * @param type
     * @return
     */
    public static boolean setLookAndFeel(String type) {
        int position = 1;
        String[] feels
                = {"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
                    "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
                };

        if (type.equalsIgnoreCase("autodetect")) {
            String operatingSystem = getOperatingSystemType();
            if (operatingSystem.equalsIgnoreCase("linux")) {
                position = 0;
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
            } else if (operatingSystem.equalsIgnoreCase("windows")) {
                position = 2;
            }
        } else if (type.equalsIgnoreCase("gtk")) {
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            position = 0;
        } else if (type.equalsIgnoreCase("windows")) {
            position = 1;
        } else if (type.equalsIgnoreCase("motif")) {
            position = 2;
        } else if (type.equalsIgnoreCase("nimbus")) {
            position = 3;
        }
        try {
            //System.out.println();
            utils.Utils.print("--> mudar look and feel");
            UIManager.setLookAndFeel(feels[position]);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            return false;
        }
        return true;
    }

    public static String getOperatingSystemType() {
        String detectedOS = null;
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                return "macOS";
            } else if (OS.indexOf("win") >= 0) {
                return "windows";
            } else if (OS.indexOf("nux") >= 0) {
                return "linux";
            } else {
                return "other";
            }
        }
        return detectedOS;
    }

    public static boolean print(String message) {
        if (Settings.isShowWarnings()) {
            System.out.println(message);
        }
        return Settings.isShowWarnings();
    }

    /**
     *
     * One time pad encryption Theoretically cannot be craked, but with NSA and
     * CIA, i really dont know
     *
     * https://en.wikipedia.org/wiki/One-time_pad
     *
     * @param encryptionKey
     * @param message
     * @return
     */
    public static String encrypt(String encryptionKey, String message) {
        String toReturn = "";
        int count = 0;
        // convert secret text to byte array
        byte[] secret = message.getBytes();

        byte[] encoded = new byte[secret.length];

        // Generate key (has to be exchanged)
        byte[] key = encryptionKey.getBytes();

        // Encrypt
        for (int i = 0; i < secret.length; i++) {
            encoded[i] = (byte) (secret[i] ^ key[count]);
            if (count >= key.length) {
                count = 0;
            } else {
                count++;
            }
        }
        toReturn = new String(encoded, StandardCharsets.UTF_8);
        return toReturn;
    }

    /**
     *
     * One time pad encryption Theoretically cannot be craked, but with NSA and
     * CIA, i really dont know
     *
     * https://en.wikipedia.org/wiki/One-time_pad
     *
     * @param encryptionKey
     * @param message
     * @return
     */
    public static String decrypt(String encryptionKey, String message) {
        String toReturn = "";
        int count = 0;
        byte[] encoded = message.getBytes();
        byte[] decoded = new byte[encoded.length];
        byte[] key = encryptionKey.getBytes();

        // Decrypt
        for (int i = 0; i < encoded.length; i++) {
            decoded[i] = (byte) (encoded[i] ^ key[count]);
            if (count >= key.length) {
                count = 0;
            } else {
                count++;
            }
        }
        toReturn = new String(decoded, StandardCharsets.UTF_8);

        return toReturn;
    }

    /**
     * Zip files into a single file, compressed and faster to send
     *
     * via tcp, and prevent files to get errors
     *
     *
     * @param filesToDownloadLocationDirectory
     * @param files
     * @param fileLocationOfZipFile
     * @return
     */
    public static File fileZipper(File filesToDownloadLocationDirectory, ArrayList<String> files, String fileLocationOfZipFile) {
        File zip = new File(fileLocationOfZipFile);
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(zip.getAbsoluteFile());
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                String absolutePath = filesToDownloadLocationDirectory.getAbsolutePath() + "/"+ filePath;
                File input = new File(absolutePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
        } catch (IOException e) {
            System.err.printf("" + e.toString());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                System.err.printf("" + ex.toString());
            }
        }
        return zip;
    }

    public static boolean fileUnziper(String zippedFileLocation, String fileLocationToExtract, boolean removeZip) {
        byte[] buffer = new byte[1024];

        try {

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFileLocation));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(fileLocationToExtract + File.separator + fileName);

                print("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            print("Done");
            if (removeZip) {
                removeFile(zippedFileLocation);
            }

        } catch (IOException ex) {
            return false;

        }

        return true;
    }

    public static boolean removeFile(String pathToFile) {
        Path path = Paths.get(pathToFile);
        try {
            Files.delete(path);
            print("File removed");
        } catch (NoSuchFileException x) {
            print(path + ": no such" + " file or directory\n");
        } catch (DirectoryNotEmptyException x) {
            print(path + " not empty\n");
        } catch (IOException x) {
            // File permission problems are caught here.
            print(x.toString());
        }
        return true;
    }
}

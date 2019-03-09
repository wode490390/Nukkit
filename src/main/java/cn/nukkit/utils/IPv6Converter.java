package cn.nukkit.utils;  

/**
 * @author：Wendy
 */
public class IPv6Converter {

    public static final int IPV6Length = 8; // IPV6地址的分段  
    public static final int IPV4Length = 4; // IPV6地址分段  
    public static final int IPV4ParmLength = 2; // 一个IPV4分段占的长度  
    public static final int IPV6ParmLength = 4; // 一个IPV6分段占的长  

    /** 
     * IPV6、IPV4转化为十六进制串 
     *  
     * @param ipAddress 
     * @return String
     */  
    public static String buildKey(String ipAddress) {  
        String Key = "";  
        // ipv4标识 。判断是否是ipv4地址  
        int dotFlag = ipAddress.indexOf('.');  
        // ipv6标识 。判断是否是ipv6地址  
        int colonFlag = ipAddress.indexOf(':');  
        // ipv6标识 。判断是否是简写的ipv6地址  
        int dColonFlag = ipAddress.indexOf("::");  
        // 将v6或v4的分隔符用&代替  
        ipAddress = ipAddress.replace(".", "&");  
        ipAddress = ipAddress.replace(":", "&");  
        // ipv4 address。将ipv4地址转换成16进制的形式  
        if (dotFlag != -1 && colonFlag == -1) {  
            String[] arr = ipAddress.split("&");  
            // 1、 ipv4转ipv6，前4组数补0或f  
            for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {  
                // 根据v4转v6的形式，除第4组数补ffff外，前3组数补0000  
                if (i == IPV6Length - IPV4ParmLength - 1) {  
                    Key += "ffff";  
                } else {  
                    Key += "0000";  
                }  
            }  
            // 2、将ipv4地址转成16进制  
            for (int j = 0; j < IPV4Length; j++) {  
                // 1)将每组ipv4地址转换成16进制  
                arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));  
                // 2) 位数不足补0，ipv4地址中一组可转换成一个十六进制，两组数即可标识ipv6中的一组，v6中的一组数不足4位补0  
                for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {  
                    Key += "0";  
                }  
                Key += arr[j];  
            }  
        }  
        // Mixed address with ipv4 and ipv6。将v4与v6的混合地址转换成16进制的形式  
        if (dotFlag != -1 && colonFlag != -1 && dColonFlag == -1) {  
            String[] arr = ipAddress.split("&");  
  
            for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {  
                // 将ip地址中每组不足4位的补0  
                for (int k = 0; k < (IPV6ParmLength - arr[i].length()); k++) {  
                    Key += "0";  
                }  
                Key += arr[i];  
            }  
  
            for (int j = 0; j < IPV4Length; j++) {  
                arr[j] = Integer.toHexString(Integer.parseInt(arr[j]));  
                for (int k = 0; k < (IPV4ParmLength - arr[j].length()); k++) {  
                    Key += "0";  
                }  
                Key += arr[j];  
            }  
        }  
        // Mixed address with ipv4 and ipv6,and there are more than one  
        // '0'。将v4与v6的混合地址(如::32:dc:192.168.62.174)转换成16进制的形式  
        // address param  
        if (dColonFlag != -1 && dotFlag != -1) {  
            String[] arr = ipAddress.split("&");  
            // 存放16进制的形式  
            String[] arrParams = new String[IPV6Length + IPV4ParmLength];  
            int indexFlag = 0;  
            int pFlag = 0;  
            // 1、将简写的ip地址补0  
            // 如果ip地址中前面部分采用简写，做如下处理  
            if ("".equals(arr[0])) {  
                // 1)如果ip地址采用简写形式，不足位置补0，存放到arrParams中  
                for (int j = 0; j < (IPV6Length + IPV4ParmLength - (arr.length - 2)); j++) {  
                    arrParams[j] = "0000";  
                    indexFlag++;  
                }  
                // 2)将已有值的部分(如32:dc:192.168.62.174)存放到arrParams中  
                for (int i = 2; i < arr.length; i++) {  
                    arrParams[indexFlag] = arr[i];  
                    indexFlag++;  
                }  
            } else {  
                for (int i = 0; i < arr.length; i++) {  
                    if ("".equals(arr[i])) {  
                        for (int j = 0; j < (IPV6Length + IPV4ParmLength  
                                - arr.length + 1); j++) {  
                            arrParams[indexFlag] = "0000";  
                            indexFlag++;  
                        }  
                    } else {  
                        arrParams[indexFlag] = arr[i];  
                        indexFlag++;  
                    }  
                }  
            }  
            // 2、ip(去除ipv4的部分)中采用4位十六进制数表示一组数，将不足4位的十六进制数补0  
            for (int i = 0; i < IPV6Length - IPV4ParmLength; i++) {  
                // 如果arrParams[i]组数据不足4位，前补0  
                for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {  
                    Key += "0";  
                }  
                Key += arrParams[i];  
                // pFlag用于标识位置，主要用来标识ipv4地址的起始位  
                pFlag++;  
            }  
            // 3、将ipv4地址转成16进制  
            for (int j = 0; j < IPV4Length; j++) {  
                // 1)将每组ipv4地址转换成16进制  
                arrParams[pFlag] = Integer.toHexString(Integer  
                        .parseInt(arrParams[pFlag]));  
                // 2)位数不足补0，ipv4地址中一组可转换成一个十六进制，两组数即可标识ipv6中的一组，v6中的一组数不足4位补0  
                for (int k = 0; k < (IPV4ParmLength - arrParams[pFlag].length()); k++) {  
                    Key += "0";  
                }  
                Key += arrParams[pFlag];  
                pFlag++;  
            }  
        }  
        // ipv6 address。将ipv6地址转换成16进制  
        if (dColonFlag == -1 && dotFlag == -1 && colonFlag != -1) {  
            String[] arrParams = ipAddress.split("&");  
            // 将v6地址转成十六进制  
            for (int i = 0; i < IPV6Length; i++) {  
                // 将ipv6地址中每组不足4位的补0  
                for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {  
                    Key += "0";  
                }  
  
                Key += arrParams[i];  
            }  
        }  
  
        if (dColonFlag != -1 && dotFlag == -1) {  
            String[] arr = ipAddress.split("&");  
            String[] arrParams = new String[IPV6Length];  
            int indexFlag = 0;  
            if ("".equals(arr[0])) {  
                for (int j = 0; j < (IPV6Length - (arr.length - 2)); j++) {  
                    arrParams[j] = "0000";  
                    indexFlag++;  
                }  
                for (int i = 2; i < arr.length; i++) {  
                    arrParams[indexFlag] = arr[i];  
                    i++;  
                    indexFlag++;  
                }  
            } else {  
                for (int i = 0; i < arr.length; i++) {  
                    if ("".equals(arr[i])) {  
                        for (int j = 0; j < (IPV6Length - arr.length + 1); j++) {  
                            arrParams[indexFlag] = "0000";  
                            indexFlag++;  
                        }  
                    } else {  
                        arrParams[indexFlag] = arr[i];  
                        indexFlag++;  
                    }  
                }  
            }  
            for (int i = 0; i < IPV6Length; i++) {  
                for (int k = 0; k < (IPV6ParmLength - arrParams[i].length()); k++) {  
                    Key += "0";  
                }  
                Key += arrParams[i];  
            }  
        }  
        return Key;  
    }  

    /** 
     * 十六进制串转化为IP地址 
     *  
     * @param key 
     * @return String
     */  
    public static String splitKey(String key) {  
        String IPV6Address = "";  
        String IPAddress = "";  
        String strKey;  
        String ip1 = key.substring(0, 24);  
        String tIP1 = ip1.replace("0000", "").trim();  
        if (!"".equals(tIP1) && !"FFFF".equals(tIP1)) {  
            // 将ip按：分隔  
            while (!"".equals(key)) {  
                strKey = key.substring(0, 4);  
                key = key.substring(4);  
                if ("".equals(IPV6Address)) {  
                    IPV6Address = strKey;  
                } else {  
                    IPV6Address += ":" + strKey;  
                }  
            }  
            IPAddress = IPV6Address;  
        }  
        return IPAddress;  
    }  

    /** 
     * 将ip地址都转成16个字节的数组。先将v6地址存以":"分隔存放到数组中，再将数组中的每两位取存到长度为16的字符串数组中， 
     * 再将这两位十六进制数转成十进制，再转成byte类型存放到16个字的数组中。 
     *  
     * @param ip 
     * @return byte[]
     */  
    public static byte[] toByte(String ip) {  
        // 将ip地址转换成16进制  
        String Key = buildKey(ip);  
        // 将16进制转换成ip地址  
        String ip6 = splitKey(Key);  
  
        // 将v6f地址存以":"分隔存放到数组中  
        String[] ip6Str = ip6.split(":");  
        String[] ipStr = new String[16];  
        byte[] ip6Byte = new byte[16];  
  
        // 将数组中的每两位取存到长度为16的字符串数组中  
        for (int j = 0, i = 0; i < ip6Str.length; j += 2, i++) {  
            ipStr[j] = ip6Str[i].substring(0, 2);  
            ipStr[j + 1] = ip6Str[i].substring(2, 4);  
        }  
  
        // 将ipStr中的十六进制数转成十进制，再转成byte类型存放到16个字的数组中  
        for (int i = 0; i < ip6Byte.length; i++) {  
            ip6Byte[i] = (byte) Integer.parseInt(ipStr[i], 16);  
        }  
        return ip6Byte;  
    }
}

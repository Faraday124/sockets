package utils;

public class CommonUtils {

    public static String withClientPrefix(String message){
        return String.format("[CLIENT] %s", message);
    }
    public static String withServerPrefix(String message){
        return String.format("[SERVER] %s", message);
    }
}

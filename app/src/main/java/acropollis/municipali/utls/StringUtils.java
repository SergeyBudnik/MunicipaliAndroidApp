package acropollis.municipali.utls;

public class StringUtils {
    public String formatStringToUpperFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length() - 1);
    }
}

package acropollis.municipali.rest.raw;

interface RestConfiguration {
    String LIVE_ISRAEL = "http://185.46.8.242:8080/Municipali";
    String LIVE_MACEDONIA = "http://52.33.169.93:8080/Municipali";
    String QA_ISRAEL = "http://185.46.9.175:8080/Municipali";
    String LOCALHOST = "http://192.168.0.198:8080";

    String BASE_URL = LIVE_ISRAEL;

    String ALPHA_BASE_URL = "http://185.46.9.175:8080/MunicipaliAlpha";
}

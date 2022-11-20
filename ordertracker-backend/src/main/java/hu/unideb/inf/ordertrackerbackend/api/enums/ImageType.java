package hu.unideb.inf.ordertrackerbackend.api.enums;

public enum ImageType {
    GENERAL,
    THUMBNAIL;

    public static ImageType findByName(String name) {
        for (ImageType type : ImageType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
package ru.clevertec.commentservice.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class Constant {

    public static final UUID NEWS_UUID = UUID.fromString("45f1ab38-8678-4271-a0b2-82f5c4de549c");

    public static final UUID COMMENT_UUID = UUID.fromString("b6c4f1bc-4553-48f1-b6c6-f5688c36fd19");
    public static final String COMMENT_TEXT = "This is a comment";
    public static final String COMMENT_AUTHOR = "doberman";

    public static final String COMMENT_TEXT_UPDATE = "This is an updated comment";

    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 10;

}

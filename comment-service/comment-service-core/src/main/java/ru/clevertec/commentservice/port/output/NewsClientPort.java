package ru.clevertec.commentservice.port.output;

import java.util.UUID;

public interface NewsClientPort {
    Boolean existsNews(UUID newsId);
}

package utils;

import lombok.Getter;

import java.util.List;

@Getter
public class PageCursor<T> {

    private CursorRequest nextCursorRequest;
    private List<T> contents;

    public PageCursor() {}

    public PageCursor(CursorRequest nextCursorRequest, List<T> contents) {
        this.nextCursorRequest = nextCursorRequest;
        this.contents = contents;
    }
}

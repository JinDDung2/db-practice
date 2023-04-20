package utils;

import lombok.Getter;

@Getter
// 커서를 사용하려면 중복 키 보장이 필수
public class CursorRequest {

    private final Long key;
    private final int size;

    public static final Long NONE_KEY = -1L;


    public CursorRequest(Long key, int size) {
        this.key = key;
        this.size = size;
    }

    public boolean hasKey() {
        return key != null;
    }

    public CursorRequest next(Long key) {
        return new CursorRequest(key, size);
    }
}

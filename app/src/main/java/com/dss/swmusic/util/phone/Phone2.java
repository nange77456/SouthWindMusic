package com.dss.swmusic.util.phone;

/**
 * 两个参数的回调接口
 * @param <T> 泛型参数1
 * @param <S> 泛型参数2
 */
public interface Phone2<T,S> {
    void onPhone(T t,S s);
}

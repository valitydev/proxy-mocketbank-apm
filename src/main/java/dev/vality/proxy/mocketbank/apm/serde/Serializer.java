package dev.vality.proxy.mocketbank.apm.serde;

public interface Serializer<T> {

    byte[] writeByte(T obj);

}

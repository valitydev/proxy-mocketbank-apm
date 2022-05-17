package dev.vality.proxy.mocketbank.apm.serde;

public interface Deserializer<T> {

    T read(byte[] data);

}

package com.shrishdeshpande.qe.util;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shrishdeshpande.qe.api.transaction.CryptoTransaction;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "ptype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CryptoTransaction.class, name = "crypto"),
        @JsonSubTypes.Type(value = ChainWrapper.class, name = "blockchain")
})
public interface IpcRepresentable {
    // common fields or methods if any
}

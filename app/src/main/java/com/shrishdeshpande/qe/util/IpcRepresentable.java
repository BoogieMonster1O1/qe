package com.shrishdeshpande.qe.util;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shrishdeshpande.qe.api.transaction.ContractTransaction;
import com.shrishdeshpande.qe.api.transaction.CryptoTransaction;
import com.shrishdeshpande.qe.api.transaction.MintTransaction;
import com.shrishdeshpande.qe.api.transaction.NftTransaction;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "ptype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CryptoTransaction.class, name = "crypto"),
        @JsonSubTypes.Type(value = MintTransaction.class, name = "mint"),
        @JsonSubTypes.Type(value = NftTransaction.class, name = "nft"),
        @JsonSubTypes.Type(value = ContractTransaction.class, name = "contract"),
        @JsonSubTypes.Type(value = ChainWrapper.class, name = "blockchain")
})
public interface IpcRepresentable {
    // common fields or methods if any
}

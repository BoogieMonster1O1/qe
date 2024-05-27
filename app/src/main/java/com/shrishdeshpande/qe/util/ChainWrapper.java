package com.shrishdeshpande.qe.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shrishdeshpande.qe.api.Block;

import java.util.List;

public record ChainWrapper(@JsonProperty("blocks") List<Block> blocks) implements IpcRepresentable {
}

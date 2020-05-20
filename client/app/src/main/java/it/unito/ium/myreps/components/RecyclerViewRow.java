package it.unito.ium.myreps.components;

import java.io.Serializable;

public interface RecyclerViewRow extends Serializable {
    String getRowTitle();
    String getRowDescription();
}

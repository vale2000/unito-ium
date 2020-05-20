package it.unito.ium.myreps.components;

import java.io.Serializable;

public abstract class RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = -2206576933005998242L;

    public abstract String getType();

    public abstract String getHeader();

    public abstract String getDescription();
}

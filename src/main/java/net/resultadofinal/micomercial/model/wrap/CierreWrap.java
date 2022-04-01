package net.resultadofinal.micomercial.model.wrap;

import java.util.List;

public class CierreWrap {
    private Long cartillaDiariaId;
    private List<CartillaDiariaCierreWrap> listaCierre;

    public Long getCartillaDiariaId() {
        return cartillaDiariaId;
    }

    public void setCartillaDiariaId(Long cartillaDiariaId) {
        this.cartillaDiariaId = cartillaDiariaId;
    }

    public List<CartillaDiariaCierreWrap> getListaCierre() {
        return listaCierre;
    }

    public void setListaCierre(List<CartillaDiariaCierreWrap> listaCierre) {
        this.listaCierre = listaCierre;
    }
}

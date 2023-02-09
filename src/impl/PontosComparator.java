package impl;

import dominio.PosicaoTabela;


public class PontosComparator {

    public static int compare(PosicaoTabela o, PosicaoTabela o2) {
        if (o.pontos() < o2.pontos()) return -1;
       else if (o.pontos()> o2.pontos()) return 1;
       else return 0;
    }
}

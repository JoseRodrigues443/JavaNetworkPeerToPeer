/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public enum ByteSizeFormat {
    /**
     * byte
     *
     * Kb
     *
     * Mb
     *
     * Gb
     *
     * Tb
     *
     * Pb
     *
     * Eb
     */
    BYTE("byte"),
    KBYTE("Kb"),
    MBYTE("Mb"),
    GBYTE("Gb"),
    TBYTE("Tb"),
    PBYTE("Pb"),
    EBYTE("Eb"),
    UNKNOWN("ERROR NOT KNOWN");

    private final String byteType;

    /**
     *
     * @param s
     */
    private ByteSizeFormat(String s) {
        byteType = s;
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean equals(ByteSizeFormat o) {
        return byteType.equalsIgnoreCase(o.toString());
    }

    @Override
    public String toString() {
        return this.byteType;
    }
}

package com.tup.reservasi.enums;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan dari class-diagram:
 * - Approval memiliki atribut keputusan: ApprovalDecision.
 * - Nilai enum yang perlu disepakati saat coding:
 *   keputusan untuk setujui().
 *   keputusan untuk tolak().
 *   keputusan untuk mintaRevisi().
 * - Nilai enum harus sinkron dengan method Approval dan ApprovalService.
 */

public enum ApprovalDecision {

    APPROVED("Disetujui"),
    REJECTED("Ditolak"),
    REVISION_REQUESTED("Minta Revisi");

    private final String label;

    ApprovalDecision(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean requiresCatatan() {
        return this == REJECTED || this == REVISION_REQUESTED;
    }
}

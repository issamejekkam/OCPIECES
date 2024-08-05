package com.ocp.demo1;

import java.time.LocalDate;

public class PieceOptions {
    private String code;
    private String numero;
    private LocalDate dae;
    private LocalDate das;
    private String dateControlePeriodique;
    private LocalDate dateFuturControle;
    private String chargeControle;
    private String decisionReforme;
    private LocalDate daeProvisoire;
    private String chargeDaeProvisoire;
    private  String ChargeDasProvisoire;
    private LocalDate dasProvisoire;


    public PieceOptions(String code, String numero, LocalDate dae, LocalDate das,
                        String dateControlePeriodique, LocalDate dateFuturControle,
                        String chargeControle, String decisionReforme,LocalDate daeProvisoire, String chargeDaeProvisoire,LocalDate dasProvisoire,String ChargeDasProvisoire) {
        this.code = code;
        this.numero = numero;
        this.dae = dae;
        this.das = das;
        this.dateControlePeriodique = dateControlePeriodique;
        this.dateFuturControle = dateFuturControle;
        this.chargeControle = chargeControle;
        this.decisionReforme = decisionReforme;
        this.daeProvisoire = daeProvisoire;
        this.chargeDaeProvisoire = chargeDaeProvisoire;
        this.ChargeDasProvisoire = ChargeDasProvisoire;
        this.dasProvisoire = dasProvisoire;
    }

    // Getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getDae() { return dae; }
    public void setDae(LocalDate dae) { this.dae = dae; }

    public LocalDate getDas() { return das; }
    public void setDas(LocalDate das) { this.das = das; }

    public String getDateControlePeriodique() { return dateControlePeriodique; }
    public void setDateControlePeriodique(String dateControlePeriodique) { this.dateControlePeriodique = dateControlePeriodique; }

    public LocalDate getDateFuturControle() { return dateFuturControle; }
    public void setDateFuturControle(LocalDate date) { this.dateFuturControle = date; }

    public String getChargeControle() { return chargeControle; }
    public void setChargeControle(String chargeControle) { this.chargeControle = chargeControle; }

    public String getDecisionReforme() { return decisionReforme; }
    public void setDecisionReforme(String decisionReforme) { this.decisionReforme = decisionReforme; }

    public LocalDate getDaeProvisoire() { return daeProvisoire; }

    public void setDaeProvisoire(LocalDate daeProvisoire) {
        this.daeProvisoire = daeProvisoire;
    }

    public String getChargeDaeProvisoire() {
        return chargeDaeProvisoire;
    }

    public void setChargeDaeProvisoire(String chargeDaeProvisoire) {
        this.chargeDaeProvisoire = chargeDaeProvisoire;
    }

    public String getChargeDasProvisoire() {
        return ChargeDasProvisoire;
    }

    public void setChargeDasProvisoire(String chargeDasProvisoire) {
        ChargeDasProvisoire = chargeDasProvisoire;
    }

    public LocalDate getDasProvisoire() {
        return dasProvisoire;
    }

    public void setDasProvisoire(LocalDate dasProvisoire) {
        this.dasProvisoire = dasProvisoire;
    }
}

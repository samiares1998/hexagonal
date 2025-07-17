-- Eliminación de tablas si existen
DROP TABLE IF EXISTS CODE CASCADE;
DROP TABLE IF EXISTS CODE_GROUP CASCADE;
DROP TABLE IF EXISTS INVOICE_TYPE CASCADE;
DROP TABLE IF EXISTS CODE_HISTORY CASCADE;
DROP TABLE IF EXISTS reissuance CASCADE;

-- Creación de tablas
CREATE TABLE CODE_GROUP (
    id_code_group UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE CODE (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    mambu_id_code INT UNIQUE NOT NULL,
    mambu_code_name VARCHAR(255) NOT NULL,
    id_code_group UUID NOT NULL,
    FOREIGN KEY (id_code_group) REFERENCES CODE_GROUP(id_code_group)
);

CREATE INDEX idx_code_mambu_id_code ON CODE (mambu_id_code);

CREATE TABLE INVOICE_TYPE (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    invoice_type_id INT UNIQUE NOT NULL,
    invoice_type_name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_invoice_type_id ON INVOICE_TYPE (invoice_type_id);

CREATE TABLE reissuance (
    id BIGSERIAL PRIMARY KEY,
    holder_id UUID,
    account_id UUID,
    old_card_id UUID,
    new_card_id UUID,
    expiration_date INTEGER,
    motive TEXT,
    status VARCHAR(50),
    card_type VARCHAR(50),
    process_type VARCHAR(50) DEFAULT 'Reposición',
    reset_user VARCHAR(100),
    reset_user_email VARCHAR(255),
    old_masked_pan VARCHAR(100),
    new_masked_pan VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de auditoría
CREATE TABLE CODE_HISTORY (
    history_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    table_name TEXT NOT NULL,
    operation TEXT NOT NULL,
    changed_at TIMESTAMPTZ DEFAULT now(),
    changed_by TEXT,
    data JSONB
);

-- Función de auditoría
CREATE OR REPLACE FUNCTION audit_changes()
RETURNS TRIGGER AS $$
DECLARE
    record_data JSONB;
BEGIN
    IF TG_OP = 'INSERT' THEN
        record_data := to_jsonb(NEW);
    ELSIF TG_OP = 'UPDATE' THEN
        record_data := jsonb_build_object('old', to_jsonb(OLD), 'new', to_jsonb(NEW));
    ELSIF TG_OP = 'DELETE' THEN
        record_data := to_jsonb(OLD);
    END IF;

    INSERT INTO CODE_HISTORY (table_name, operation, changed_by, data)
    VALUES (TG_TABLE_NAME, TG_OP, current_setting('audit.username', true), record_data);

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Triggers de auditoría
CREATE TRIGGER trg_code_audit
AFTER INSERT OR UPDATE OR DELETE ON CODE
FOR EACH ROW EXECUTE FUNCTION audit_changes();

CREATE TRIGGER trg_code_group_audit
AFTER INSERT OR UPDATE OR DELETE ON CODE_GROUP
FOR EACH ROW EXECUTE FUNCTION audit_changes();

-- Inserción de datos en CODE_GROUP
INSERT INTO CODE_GROUP (id_code_group, group_name)
VALUES
    (gen_random_uuid(), 'Abono Consumo Refinanciado'),
    (gen_random_uuid(), 'Abono Consumo Renegociado'),
    (gen_random_uuid(), 'Abono Crédito Consumo'),
    (gen_random_uuid(), 'Abono DAP CLP'),
    (gen_random_uuid(), 'Abono DAP CLF'),
    (gen_random_uuid(), 'Abono intereses garantía TC'),
    (gen_random_uuid(), 'Ajustes'),
    (gen_random_uuid(), 'Anticipo de caja'),
    (gen_random_uuid(), 'Avance en efectivo TC'),
    (gen_random_uuid(), 'Canje premios'),
    (gen_random_uuid(), 'Cargo garantía TC'),
    (gen_random_uuid(), 'Comisión apertura CCTE'),
    (gen_random_uuid(), 'Comisión DAP'),
    (gen_random_uuid(), 'Comisión Tarjeta de débito'),
    (gen_random_uuid(), 'Compra internacional'),
    (gen_random_uuid(), 'Compra nacional'),
    (gen_random_uuid(), 'Compra Redpay'),
    (gen_random_uuid(), 'Cuenta remunerada'),
    (gen_random_uuid(), 'Devolución garantía TC'),
    (gen_random_uuid(), 'Fraude'),
    (gen_random_uuid(), 'Giro ATM Internacional'),
    (gen_random_uuid(), 'Giro ATM Nacional'),
    (gen_random_uuid(), 'Inversión DAP CLF'),
    (gen_random_uuid(), 'Inversión DAP CLP'),
    (gen_random_uuid(), 'Inversiones'),
    (gen_random_uuid(), 'IVA Digital'),
    (gen_random_uuid(), 'LBTR'),
    (gen_random_uuid(), 'Moneysend'),
    (gen_random_uuid(), 'Pago cuota Consumo'),
    (gen_random_uuid(), 'Pago cuota Consumo Refinanciado'),
    (gen_random_uuid(), 'Pago cuota Consumo Renegociado'),
    (gen_random_uuid(), 'Pago cuota TC'),
    (gen_random_uuid(), 'Pago de cuentas'),
    (gen_random_uuid(), 'Pago por nómina'),
    (gen_random_uuid(), 'Paypal'),
    (gen_random_uuid(), 'POS Klap'),
    (gen_random_uuid(), 'Prepago parcial Consumo'),
    (gen_random_uuid(), 'Prepago parcial Consumo Refinanciado'),
    (gen_random_uuid(), 'Prepago parcial Consumo Renegociado'),
    (gen_random_uuid(), 'Prepago total Consumo'),
    (gen_random_uuid(), 'Prepago total Consumo Refinanciado'),
    (gen_random_uuid(), 'Prepago total Consumo Renegociado'),
    (gen_random_uuid(), 'Recarga servicios'),
    (gen_random_uuid(), 'Regularización CCTE Centinela'),
    (gen_random_uuid(), 'Remesas'),
    (gen_random_uuid(), 'Retenciones judiciales'),
    (gen_random_uuid(), 'Saldo Inmobilizado CCTE'),
    (gen_random_uuid(), 'Servipag'),
    (gen_random_uuid(), 'Suscripción internacional'),
    (gen_random_uuid(), 'Suscripción nacional'),
    (gen_random_uuid(), 'Tasa intercambio tarjeta de débito'),
    (gen_random_uuid(), 'Teenpo'),
    (gen_random_uuid(), 'Telemedicina'),
    (gen_random_uuid(), 'Tenpo Pro'),
    (gen_random_uuid(), 'Tenpoprotección familia'),
    (gen_random_uuid(), 'Transbank'),
    (gen_random_uuid(), 'Transferencia bancaria'),
    (gen_random_uuid(), 'Transferencia P2P'),
    (gen_random_uuid(), 'Vales vista');

-- Inserción de datos en CODE usando JOIN con CODE_GROUP
INSERT INTO CODE (id, mambu_id_code, mambu_code_name, id_code_group)
SELECT
    gen_random_uuid(),
    c.mambu_id_code,
    c.mambu_code_name,
    g.id_code_group
FROM
    (VALUES
        (123 , 'AB_CARTERA_REF', 'Abono Consumo Refinanciado'),
              (304 , 'REV_AB_CARTERA_REF', 'Abono Consumo Refinanciado'),
              (151 , 'AB_CARTERA_RENE', 'Abono Consumo Renegociado'),
              (332 , 'REV_AB_CARTERA_RENE', 'Abono Consumo Renegociado'),
              (95 , 'AB_CCTE_CC', 'Abono Crédito Consumo'),
              (276 , 'REV_AB_CCTE_CC', 'Abono Crédito Consumo'),
              (78 , 'AB_CCTE_RESC_ANTCPDO_DAPCLP', 'Abono DAP CLP'),
              (259 , 'REV_AB_CCTE_RTRO_ANTCPDO_DAPCLP', 'Abono DAP CLP'),
              (76 , 'AB_CTE_CTE_RESC_DAPCLP', 'Abono DAP CLP'),
              (257 , 'REV_AB_CTE_CTE_RESC_DAPCLP', 'Abono DAP CLP'),
              (79 , 'AB_CCTE_RESC_ANTCPDO_DAPCLF', 'Abono DAP CLF'),
              (260 , 'REV_AB_CCTE_RTRO_ANTCPDO_DAPCLF', 'Abono DAP CLF'),
              (77 , 'AB_CTE_CTE_RESC_DAPCLF', 'Abono DAP CLF'),
              (258 , 'REV_AB_CTE_CTE_RESC_DAPCLF', 'Abono DAP CLF'),
              (73 , 'AB_INTERES_GARANTIA', 'Abono intereses garantía TC'),
              (254 , 'REV_AB_INTERES_GARANTIA', 'Abono intereses garantía TC'),
              (13 , 'AJUSTE_NEGATIVO', 'Ajustes'),
              (194 , 'REV_AJUSTE_NEGATIVO', 'Ajustes'),
              (14 , 'AJUSTE_POSITIVO', 'Ajustes'),
              (195 , 'REV_AJUSTE_POSITIVO', 'Ajustes'),
              (49 , 'ANTICIPO_DE_CAJA', 'Anticipo de caja'),
              (230 , 'REV_ANTICIPO_DE_CAJA', 'Anticipo de caja'),
              (68 , 'AB_AVANCE_EFECTIVO_TC', 'Avance en efectivo TC'),
              (249 , 'REV_AB_AVANCE_EFECTIVO_TC', 'Avance en efectivo TC'),
              (25 , 'CANJE_TENPESOS', 'Canje premios'),
              (206 , 'REV_CANJE_TENPESOS', 'Canje premios'),
              (24 , 'PREMIOS', 'Canje premios'),
              (205 , 'REV_PREMIOS', 'Canje premios'),
              (69 , 'CARGO_GARANTIA_TC', 'Cargo garantía TC'),
              (250 , 'REV_CARGO_GARANTIA_TC', 'Cargo garantía TC'),
              (20 , 'COMISION_APERTURA_CCTE', 'Comisión apertura CCTE'),
              (201 , 'REV_COMISION_APERTURA_CCTE', 'Comisión apertura CCTE'),
              (80 , 'COMISION_DAP', 'Comisión DAP'),
              (261 , 'REV_COMISION_DAP', 'Comisión DAP'),
              (15 , 'COMISION_REEM_TRJTA_DEBITO', 'Comisión Tarjeta de débito'),
              (196 , 'REV_COMISION_REEM_TRJTA_DEBITO', 'Comisión Tarjeta de débito'),
              (1 , 'COMPRA_INTERNACIONAL', 'Compra internacional'),
              (182 , 'REV_COMPRA_INTERNACIONAL', 'Compra internacional'),
              (3 , 'DEV_COMPRA_INTERNACIONAL', 'Compra internacional'),
              (184 , 'REV_DEV_COMPRA_INTERNACIONAL', 'Compra internacional'),
              (53 , 'COMPR_INTER_FACT_NACIONAL', 'Compra internacional'),
              (234 , 'REV_COMPR_INTER_FACT_NACIONAL', 'Compra internacional'),
              (54 , 'DEV_COMPR_INTER_FACT_NAC', 'Compra internacional'),
              (235 , 'REV_DEV_COMPR_INTER_FACT_NAC', 'Compra internacional'),
              (55 , 'COMPRA_INTERNACIONAL_CLP', 'Compra internacional'),
              (236 , 'REV_COMPRA_INTERNACIONAL_CLP', 'Compra internacional'),
              (56 , 'DEV_COMPRA_INTERNACIONAL_CLP', 'Compra internacional'),
              (237 , 'REV_DEV_COMPRA_INTERNACIONAL_CLP', 'Compra internacional'),
              (52 , 'CASHBACK_COMPRA_INTER', 'Compra internacional'),
              (233 , 'REV_CASHBACK_COMPRA_INTER', 'Compra internacional'),
              (2 , 'COMPRA_NACIONAL', 'Compra nacional'),
              (183 , 'REV_COMPRA_NACIONAL', 'Compra nacional'),
              (4 , 'DEV_COMPRA_NACIONAL', 'Compra nacional'),
              (185 , 'REV_DEV_COMPRA_NACIONAL', 'Compra nacional'),
              (51 , 'CASHBACK_COMPRA_NACIONAL', 'Compra nacional'),
              (232 , 'REV_CASHBACK_COMPRA_NACIONAL', 'Compra nacional'),
              (26 , 'COMPRA_REDPAY', 'Compra Redpay'),
              (207 , 'REV_COMPRA_REDPAY', 'Compra Redpay'),
              (43 , 'ABONO_CUENTA_REMUNERADA', 'Cuenta remunerada'),
              (224 , 'REV_ABONO_CUENTA_REMUNERADA', 'Cuenta remunerada'),
              (70 , 'DEV_GARANTIA_TC', 'Devolución garantía TC'),
              (251 , 'REV_DEV_GARANTIA_TC', 'Devolución garantía TC'),
              (37 , 'DEV_POR_FRAUDE', 'Fraude'),
              (218 , 'REV_DEV_POR_FRAUDE', 'Fraude'),
              (16 , 'COMISION_RTRO_ATM_INTERNAC', 'Giro ATM Internacional'),
              (197 , 'REV_COMISION_RTRO_ATM_INTERNAC', 'Giro ATM Internacional'),
              (9 , 'GIRO_DESDE_ATM_INT', 'Giro ATM Internacional'),
              (190 , 'REV_GIRO_DESDE_ATM_INT', 'Giro ATM Internacional'),
              (19 , 'DEV_COMISION_RETIRO_ATM_INTE', 'Giro ATM Internacional'),
              (200 , 'REV_DEV_COMISION_RETIRO_ATM_INTE', 'Giro ATM Internacional'),
              (10 , 'GIRO_ATM_NAC', 'Giro ATM Nacional'),
              (191 , 'REV_GIRO_ATM_NAC', 'Giro ATM Nacional'),
              (17 , 'COMISION_RETIRO_ATM_NAC', 'Giro ATM Nacional'),
              (198 , 'REV_COMISION_RETIRO_ATM_NAC', 'Giro ATM Nacional'),
              (18 , 'DEV_COMISION_RETIRO_ATM_NAC', 'Giro ATM Nacional'),
              (199 , 'REV_DEV_COMISION_RETIRO_ATM_NAC', 'Giro ATM Nacional'),
              (88 , 'RTRO_CCTE_PARA_DAPCLF', 'Inversión DAP CLF'),
              (269 , 'REV_RTRO_CCTE_PARA_DAPCLF', 'Inversión DAP CLF'),
              (87 , 'RTRO_CCTE_PARA_DAPCLP', 'Inversión DAP CLP'),
              (268 , 'REV_RTRO_CCTE_PARA_DAPCLP', 'Inversión DAP CLP'),
              (61 , 'INVERSION_BOLSILLO', 'Inversiones'),
              (242 , 'REV_INVERSION_BOLSILLO', 'Inversiones'),
              (62 , 'INVERSION_BOLSILLO_AUT', 'Inversiones'),
              (243 , 'REV_INVERSION_BOLSILLO_AUT', 'Inversiones'),
              (63 , 'INVERSION_TYBA', 'Inversiones'),
              (244 , 'REV_INVERSION_TYBA', 'Inversiones'),
              (64 , 'INVERSION_TYBA_AUT', 'Inversiones'),
              (245 , 'REV_INVERSION_TYBA_AUT', 'Inversiones'),
              (65 , 'RESC_BOLSILLO', 'Inversiones'),
              (246 , 'REV_RESC_BOLSILLO', 'Inversiones'),
              (66 , 'RESC_TYBA', 'Inversiones'),
              (247 , 'REV_RESC_TYBA', 'Inversiones'),
              (67 , 'COMPENSACION_TYBA', 'Inversiones'),
              (248 , 'REV_COMPENSACION_TYBA', 'Inversiones'),
              (181 , 'IVA_DIGITAL_INT_TD', 'IVA Digital'),
              (362 , 'REV_IVA_DIGITAL_INT_TD', 'IVA Digital'),
              (39 , 'TRANS_LBTR_DESDE_OTRO_BCO', 'LBTR'),
              (220 , 'REV_TRANS_LBTR_DESDE_OTRO_BCO', 'LBTR'),
              (40 , 'TRANS_LBTR_HACIA_OTRO_BCO', 'LBTR'),
              (221 , 'REV_TRANS_LBTR_HACIA_OTRO_BCO', 'LBTR'),
              (50 , 'MONEYSEND_BANCO', 'Moneysend'),
              (231 , 'REV_MONEYSEND_BANCO', 'Moneysend'),
              (99 , 'PAGO_CUOTA_CC_PAC', 'Pago cuota Consumo'),
              (280 , 'REV_PAGO_CUOTA_CC_PAC', 'Pago cuota Consumo'),
              (97 , 'PAGO_CUOTA_CC_DESDE_CCTE', 'Pago cuota Consumo'),
              (278 , 'REV_PAGO_CUOTA_CC_DESDE_CCTE', 'Pago cuota Consumo'),
              (101 , 'PAGO_CUOTA_CC_OTROS_CANALES', 'Pago cuota Consumo'),
              (282 , 'REV_PAGO_CUOTA_CC_OTROS_CANALES', 'Pago cuota Consumo'),
              (103 , 'PAGO_CUOTA_CC_SERVIPAG', 'Pago cuota Consumo'),
              (284 , 'REV_PAGO_CUOTA_CC_SERVIPAG', 'Pago cuota Consumo'),
              (117 , 'PAGO_AUT_MORA_CC', 'Pago cuota Consumo'),
              (298 , 'REV_PAGO_AUT_MORA_CC', 'Pago cuota Consumo'),
              (127 , 'PAGO_CUOTA_REF_PAC', 'Pago cuota Consumo Refinanciado'),
              (308 , 'REV_PAGO_CUOTA_REF_PAC', 'Pago cuota Consumo Refinanciado'),
              (125 , 'PAGO_CUOTA_REF_DESDE_CCTE', 'Pago cuota Consumo Refinanciado'),
              (306 , 'REV_PAGO_CUOTA_REF_DESDE_CCTE', 'Pago cuota Consumo Refinanciado'),
              (129 , 'PAGO_CUOTA_REF_OTROS_CANALES', 'Pago cuota Consumo Refinanciado'),
              (310 , 'REV_PAGO_CUOTA_REF_OTROS_CANALES', 'Pago cuota Consumo Refinanciado'),
              (131 , 'PAGO_CUOTA_REF_SERVIPAG', 'Pago cuota Consumo Refinanciado'),
              (312 , 'REV_PAGO_CUOTA_REF_SERVIPAG', 'Pago cuota Consumo Refinanciado'),
              (145 , 'PAGO_AUT_MORA_REF_CC', 'Pago cuota Consumo Refinanciado'),
              (326 , 'REV_PAGO_AUT_MORA_REF_CC', 'Pago cuota Consumo Refinanciado'),
              (155 , 'PAGO_CUOTA_RENE_PAC', 'Pago cuota Consumo Renegociado'),
              (336 , 'REV_PAGO_CUOTA_RENE_PAC', 'Pago cuota Consumo Renegociado'),
              (153 , 'PAGO_CUOTA_RENE_DESDE_CCTE', 'Pago cuota Consumo Renegociado'),
              (334 , 'REV_PAGO_CUOTA_RENE_DESDE_CCTE', 'Pago cuota Consumo Renegociado'),
              (157 , 'PAGO_CUOTA_RENE_OTRO_CANAL', 'Pago cuota Consumo Renegociado'),
              (338 , 'REV_PAGO_CUOTA_RENE_OTRO_CANAL', 'Pago cuota Consumo Renegociado'),
              (159 , 'PAGO_CUOTA_RENE_SERVIPAG', 'Pago cuota Consumo Renegociado'),
              (340 , 'REV_PAGO_CUOTA_RENE_SERVIPAG', 'Pago cuota Consumo Renegociado'),
              (173 , 'PAGO_AUT_MORA_RENE_CC', 'Pago cuota Consumo Renegociado'),
              (354 , 'REV_PAGO_AUT_MORA_RENE_CC', 'Pago cuota Consumo Renegociado'),
              (71 , 'PAGO_CUOTA_TC_AUTO_CCTE', 'Pago cuota TC'),
              (252 , 'REV_PAGO_CUOTA_TC_AUTO_CCTE', 'Pago cuota TC'),
              (74 , 'PAGO_CUOTA_TC_CCTE', 'Pago cuota TC'),
              (255 , 'REV_PAGO_CUOTA_TC_CCTE', 'Pago cuota TC'),
              (75 , 'PAGO_DESDE_CCTE_MORA_TC', 'Pago cuota TC'),
              (256 , 'REV_PAGO_DESDE_CCTE_MORA_TC', 'Pago cuota TC'),
              (72 , 'REGULARIZACION_TC_CENTINELA', 'Pago cuota TC'),
              (253 , 'REV_REGULARIZACION_TC_CENTINELA', 'Pago cuota TC'),
              (6 , 'PAGO_DE_CUENTAS', 'Pago de cuentas'),
              (187 , 'REV_PAGO_DE_CUENTAS', 'Pago de cuentas'),
              (7 , 'PAGO_DE_CUENTAS_AUTO', 'Pago de cuentas'),
              (188 , 'REV_PAGO_DE_CUENTAS_AUTO', 'Pago de cuentas'),
              (8 , 'PAGO_POR_NOMINA', 'Pago por nómina'),
              (189 , 'REV_PAGO_POR_NOMINA', 'Pago por nómina'),
              (59 , 'RETIRO_PAYPAL', 'Paypal'),
              (240 , 'REV_RETIRO_PAYPAL', 'Paypal'),
              (60 , 'CARGA_PAYPAL', 'Paypal'),
              (241 , 'REV_CARGA_PAYPAL', 'Paypal'),
              (29 , 'CARGA_POS_KLAP', 'POS Klap'),
              (210 , 'REV_CARGA_POS_KLAP', 'POS Klap'),
              (30 , 'RETIRO_POS_KLAP', 'POS Klap'),
              (211 , 'REV_RETIRO_POS_KLAP', 'POS Klap'),
              (105 , 'PREPAGO_PARC_CC_DESDE_CCTE', 'Prepago parcial Consumo'),
              (286 , 'REV_PREPAGO_PARC_CC_DESDE_CCTE', 'Prepago parcial Consumo'),
              (107 , 'PREP_PARC_CC_OTROS_CANALES', 'Prepago parcial Consumo'),
              (288 , 'REV_PREP_PARC_CC_OTROS_CANALES', 'Prepago parcial Consumo'),
              (109 , 'PREPAGO_PARC_CC_SERVIPAG', 'Prepago parcial Consumo'),
              (290 , 'REV_PREPAGO_PARC_CC_SERVIPAG', 'Prepago parcial Consumo'),
              (133 , 'PREPAGO_PARC_REF_DESDE_CCTE', 'Prepago parcial Consumo Refinanciado'),
              (314 , 'REV_PREPAGO_PARC_REF_DESDE_CCTE', 'Prepago parcial Consumo Refinanciado'),
              (135 , 'PREP_PARC_REF_OTROS_CANALES', 'Prepago parcial Consumo Refinanciado'),
              (316 , 'REV_PREP_PARC_REF_OTROS_CANALES', 'Prepago parcial Consumo Refinanciado'),
              (137 , 'PREPAGO_PARC_REF_SERVIPAG', 'Prepago parcial Consumo Refinanciado'),
              (318 , 'REV_PREPAGO_PARC_REF_SERVIPAG', 'Prepago parcial Consumo Refinanciado'),
              (161 , 'PREPAGO_PARC_RENE_DESDE_CCTE', 'Prepago parcial Consumo Renegociado'),
              (342 , 'REV_PREPAGO_PARC_RENE_DESDE_CCTE', 'Prepago parcial Consumo Renegociado'),
              (163 , 'PREP_PARC_RENE_OTROS_CANALES', 'Prepago parcial Consumo Renegociado'),
              (344 , 'REV_PREP_PARC_RENE_OTROS_CANALES', 'Prepago parcial Consumo Renegociado'),
              (165 , 'PREPAGO_PARC_RENE_SERVIPAG', 'Prepago parcial Consumo Renegociado'),
              (346 , 'REV_PREPAGO_PARC_RENE_SERVIPAG', 'Prepago parcial Consumo Renegociado'),
              (111 , 'PREPAGO_TOTAL_CC_DESDE_CCTE', 'Prepago total Consumo'),
              (292 , 'REV_PREPAGO_TOTAL_CC_DESDE_CCTE', 'Prepago total Consumo'),
              (113 , 'PREP_TOTAL_CC_OTROS_CANALES', 'Prepago total Consumo'),
              (294 , 'REV_PREP_TOTAL_CC_OTROS_CANALES', 'Prepago total Consumo'),
              (115 , 'PREPAGO_TOTAL_CC_SERVIPAG', 'Prepago total Consumo'),
              (296 , 'REV_PREPAGO_TOTAL_CC_SERVIPAG', 'Prepago total Consumo'),
              (139 , 'PREPAGO_TOTAL_REF_DESDE_CCTE', 'Prepago total Consumo Refinanciado'),
              (320 , 'REV_PREPAGO_TOTAL_REF_DESDE_CCTE', 'Prepago total Consumo Refinanciado'),
              (141 , 'PREP_TOTAL_REF_OTROS_CANALES', 'Prepago total Consumo Refinanciado'),
              (322 , 'REV_PREP_TOTAL_REF_OTROS_CANALES', 'Prepago total Consumo Refinanciado'),
              (143 , 'PREPAGO_TOTAL_REF_SERVIPAG', 'Prepago total Consumo Refinanciado'),
              (324 , 'REV_PREPAGO_TOTAL_REF_SERVIPAG', 'Prepago total Consumo Refinanciado'),
              (167 , 'PREP_TOTAL_RENE_DESDE_CCTE', 'Prepago total Consumo Renegociado'),
              (348 , 'REV_PREP_TOTAL_RENE_DESDE_CCTE', 'Prepago total Consumo Renegociado'),
              (169 , 'PREP_TOTAL_RENE_OTRO_CANAL', 'Prepago total Consumo Renegociado'),
              (350 , 'REV_PREP_TOTAL_RENE_OTRO_CANAL', 'Prepago total Consumo Renegociado'),
              (171 , 'PREPAGO_TOTAL_RENE_SERVIPAG', 'Prepago total Consumo Renegociado'),
              (352 , 'REV_PREPAGO_TOTAL_RENE_SERVIPAG', 'Prepago total Consumo Renegociado'),
              (22 , 'RECARGA_SERVICIOS_AUTO', 'Recarga servicios'),
              (203 , 'REV_RECARGA_SERVICIOS_AUTO', 'Recarga servicios'),
              (23 , 'RECARGA_SERVICIOS_MANUAL', 'Recarga servicios'),
              (204 , 'REV_RECARGA_SERVICIOS_MANUAL', 'Recarga servicios'),
              (41 , 'REGULARIZACION_CCTE_CENTINE', 'Regularización CCTE Centinela'),
              (222 , 'REV_REGULARIZACION_CCTE_CENTINE', 'Regularización CCTE Centinela'),
              (46 , 'TRANSF_EXTRANJERO', 'Remesas'),
              (227 , 'REV_TRANSF_EXTRANJERO', 'Remesas'),
              (48 , 'RET_JUD_CTACTE', 'Retenciones judiciales'),
              (229 , 'REV_RET_JUD_CTACTE', 'Retenciones judiciales'),
              (42 , 'SALDOS_INMOBILIZADOS_CCTE', 'Saldo Inmobilizado CCTE'),
              (223 , 'REV_SALDOS_INMOBILIZADOS_CCTE', 'Saldo Inmobilizado CCTE'),
              (27 , 'DEPOSITO_EFECTIVO_SERVIPAG', 'Servipag'),
              (208 , 'REV_DEPOSITO_EFECTIVO_SERVIPAG', 'Servipag'),
              (28 , 'RTRO_EFECTIVO_SERVIPAG_', 'Servipag'),
              (209 , 'REV_RTRO_EFECTIVO_SERVIPAG', 'Servipag'),
              (44 , 'SUSCRIPCION_INTERNACIONAL', 'Suscripción internacional'),
              (225 , 'REV_SUSCRIPCION_INTERNACIONAL', 'Suscripción internacional'),
              (45 , 'SUSCRIPCION_NACIONAL', 'Suscripción nacional'),
              (226 , 'REV_SUSCRIPCION_NACIONAL', 'Suscripción nacional'),
              (179 , 'TASA_INTERCAMBIO_TD', 'Tasa intercambio tarjeta de débito'),
              (360 , 'REV_TASA_INTERCAMBIO_TD', 'Tasa intercambio tarjeta de débito'),
              (180 , 'IVA_TASA_INTERCAMBIO_TD', 'Tasa intercambio tarjeta de débito'),
              (361 , 'REV_IVA_TASA_INTERCAMBIO_TD', 'Tasa intercambio tarjeta de débito'),
              (5 , 'PAGO_COMISION_TEENPO', 'Teenpo'),
              (186 , 'REV_PAGO_COMISION_TEENPO', 'Teenpo'),
              (58 , 'COBRO_TELEMEDICINA_TENPO', 'Telemedicina'),
              (239 , 'REV_COBRO_TELEMEDICINA_TENPO', 'Telemedicina'),
              (21 , 'COMISION_APERTURA_TENPO_PRO', 'Tenpo Pro'),
              (202 , 'REV_COMISION_APERTURA_TENPO_PRO', 'Tenpo Pro'),
              (57 , 'COBRO_TENPROTECCION_FAMILIA', 'Tenpoprotección familia'),
              (238 , 'REV_COBRO_TENPROTECCION_FAMILIA', 'Tenpoprotección familia'),
              (47 , 'CASH_IN_TRANSBANK', 'Transbank'),
              (228 , 'REV_CASH_IN_TRANSABANK', 'Transbank'),
              (31 , 'RTRO_TRANSF_BANCARIA', 'Transferencia bancaria'),
              (212 , 'REV_RTRO_TRANSF_BANCARIA', 'Transferencia bancaria'),
              (32 , 'CARGA_TRANSFERENCIA', 'Transferencia bancaria'),
              (213 , 'REV_CARGA_TRANSFERENCIA', 'Transferencia bancaria'),
              (11 , 'CARGO_TRASPASO_P2P', 'Transferencia P2P'),
              (192 , 'REV_CARGO_TRASPASO_P2P', 'Transferencia P2P'),
              (12 , 'ABONO_TRASPASO_P2P', 'Transferencia P2P'),
              (193 , 'REV_ABONO_TRASPASO_P2P', 'Transferencia P2P'),
              (33 , 'DEPOSITO_VALE_VISTA_OTRO_BCO', 'Vales vista'),
              (214 , 'REV_DEPOSITO_VALE_VISTA_OTRO_BCO', 'Vales vista'),
              (34 , 'EMISION_VALE_VISTA_OTRO_BCO', 'Vales vista'),
              (215 , 'REV_EMISION_VALE_VISTA_OTRO_BCO', 'Vales vista')
    ) AS c (mambu_id_code, mambu_code_name, group_name)
JOIN CODE_GROUP g ON c.group_name = g.group_name;

-- Actualización de process_type en reissuance si es NULL o vacío
UPDATE reissuance
SET process_type = 'Reposición'
WHERE process_type IS NULL OR process_type = '';

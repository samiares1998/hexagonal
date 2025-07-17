package cl.tenpo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EnumCurrency {
  CLP(152), // Peso Chileno
  USD(840), // Dólar Estadounidense
  EUR(978), // Euro
  GBP(826), // Libra Esterlina
  JPY(392), // Yen Japonés
  CAD(124), // Dólar Canadiense
  AUD(36), // Dólar Australiano
  CNY(156), // Yuan Chino
  INR(356), // Rupia India
  SEK(752), // Corona Sueca
  CHF(756), // Franco Suizo
  NOK(578), // Corona Noruega
  KRW(410), // Won Surcoreano
  BRL(986), // Real Brasileño
  RUB(643), // Rublo Ruso
  VND(704), // Dong Vietnamita
  SGD(702), // Dólar de Singapur
  HKD(344), // Dólar de Hong Kong
  NZD(554), // Dólar Neozelandés
  CZK(203), // Corona Checa
  HRK(191), // Kuna Croata
  HUF(348), // Florín Húngaro
  COP(170), // Peso Colombiano
  MXN(484), // Peso Mexicano
  MYR(458), // Ringgit Malayo
  IDR(360), // Rupia Indonesia
  PHP(608), // Peso Filipino
  THB(764); // Baht Tailandés

  private final int code;

  public static EnumCurrency fromCode(int code) {
    return Arrays.stream(EnumCurrency.values())
        .filter(currency -> currency.code == code)
        .findFirst()
        .orElse(null); // O lanzar una excepción si prefieres
  }
}

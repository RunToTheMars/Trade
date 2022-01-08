public class TradeRow {

    public Long m_tradeno;      // - номер сделки
    public Integer m_tradetime; // - время
    public String m_secboard;   // - площадка
    public String m_seccode;    // - код ценной бумаги
    public Double m_price;      // - цена
    public Long m_volume;       // - кол-во бумаг
    public Double m_accruedint; // - НКД (для облигаций)
    public Double m_yield;      // - доходность (для облигаций)
    public Double m_value;      // - сумма сделки.

    public TradeRow(String line) {
        String[] tokens = line.split("\t");
        int col_i = 0;
        m_tradeno = Long.parseLong(tokens[col_i++]);
        m_tradetime = Integer.parseInt(tokens[col_i++]);
        m_secboard = tokens[col_i++];
        m_seccode = tokens[col_i++];
        m_price = Double.parseDouble(tokens[col_i++]);
        m_volume = Long.parseLong(tokens[col_i++]);
        m_accruedint = Double.parseDouble(tokens[col_i++]);
        m_yield = Double.parseDouble(tokens[col_i++]);
        m_value = Double.parseDouble(tokens[col_i]);
    }

    public static TradeRow FromString(String line) {
        return new TradeRow(line);
    }

    public String SecCode () {
        return m_seccode;
    }

    public Double Value () {
        return m_value;
    }
}

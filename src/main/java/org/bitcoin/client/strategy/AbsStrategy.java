package org.bitcoin.client.strategy;

import org.bitcoin.market.AbstractMarketApi;
import org.bitcoin.market.MarketApiFactory;
import org.bitcoin.market.bean.Market;
import org.bitcoin.market.bean.Symbol;
import org.bitcoin.market.bean.SymbolPair;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by handong on 17/1/16.
 */
public abstract class AbsStrategy {
    private double buyPrice = 0;
    private StrategyParam strategyParam;
    private double sellPrice;
    private boolean isBuy;
    protected FileWriter fileWriter;


    public AbsStrategy(StrategyParam param) {
        this.strategyParam = param;
    }

    public abstract void firstBuy() throws IOException;

    public abstract void sell() throws IOException;

    public abstract void buy() throws IOException;

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public StrategyParam getStrategyParam() {
        return strategyParam;
    }

    public void setStrategyParam(StrategyParam strategyParam) {
        this.strategyParam = strategyParam;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }


    public void log(String log) {
        System.out.println(getStrategyParam().getSymbol().name() +"::"+ log);
    }


    public Double getTicker() throws IOException {
        double cur = getMarket().ticker(new SymbolPair(getStrategyParam().getSymbol(),
                Symbol.cny));

        return cur;
    }

    public AbstractMarketApi getMarket() {
        AbstractMarketApi market = MarketApiFactory.getInstance().getMarket(Market.PeatioCNY);
        return market;
    }


    public void writeFile(String ticker) throws IOException {
        fileWriter.write(ticker + "\n");
        fileWriter.flush();
    }

    public void trade() throws IOException {
        firstBuy();
        isBuy = false;

        while (true) {
            if (!isBuy) {
                sell();
                isBuy = true;
            } else {
                buy();
                isBuy = false;
            }
        }

    }

    public double getFirstAsk() {
        return getMarket().get_depth(new SymbolPair(getStrategyParam().getSymbol(),
                Symbol.cny),true).getJSONArray("asks").
                getJSONObject(0).getDouble("price");
    }
    public double getFirstBid() {
        return getMarket().get_depth(new SymbolPair(getStrategyParam().getSymbol(),
                Symbol.cny),true).getJSONArray("bids").
                getJSONObject(0).getDouble("price");
    }
}


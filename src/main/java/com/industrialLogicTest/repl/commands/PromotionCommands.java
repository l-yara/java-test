package com.industrialLogicTest.repl.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import com.google.common.base.Splitter;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.domain.Promotion;
import com.industrialLogicTest.repl.ReplSessionState;

/**
 * A set of commands (extensions of {@linkplain ReplCommand} working with the {@linkplain com.industrialLogicTest.domain.Promotion}: add/remove items.
 * <p>
 * As promotions do not have convenient Ids, we refer them by order no when delete.
 */
public class PromotionCommands {
    private static final Splitter PROMOTION_ARGUMENT_SPLITTER = Splitter.on("|").trimResults().omitEmptyStrings();
    public static final ReplCommand REMOVE_PROMOTION = new ReplCommand("remove-promo",
            "Remove existing Promotion by its order number. Syntax: <promotionNo>. Example: remove-promo 1") {
        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            try {
                int index = Integer.parseInt(arguments);
                List<Promotion> promotions = session.getPromotions();
                if (index < 0 || index >= promotions.size()) {
                    return "Promotion no must be in 0.." + (promotions.size() - 1) + " range";
                }
                Promotion removed = promotions.remove(index);
                return "Removed promotion " + removed;
            } catch (NumberFormatException e) {
                return "Unable to parse order no out of '" + arguments + "'";
            }
        }
    };

    public static final ReplCommand ADD_PROMOTION = new ReplCommand("add-promo",
            "Add new Promotion. Syntax: <description>|<validFrom>|<validTo>|<expression>. Example: add-promo 5% on basket > 20.0|2020-02-01|2020-03-15|totalPrice() > 20.0 ? totalPrice() * 0.05 : 0") {
        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            List<String> strings = PROMOTION_ARGUMENT_SPLITTER.splitToList(arguments);
            if (strings.size() < 4) {
                return "Unable to parse promotion data from '" + arguments + "; expecting '<description>:<validFrom>:<validTo>:<expression>'";
            }
            try {
                LocalDate validFrom = LocalDate.parse(strings.get(1));
                LocalDate validTo = LocalDate.parse(strings.get(2));
                Promotion promotion = new Promotion(strings.get(0), validFrom, validTo, strings.get(3));
                session.getPromotions().add(promotion);
                return "Promotion added: '" + promotion + "'";
            } catch (Exception e) {
                return "Unable to parse dates no out of '" + arguments + "'";
            }
        }
    };


}

package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DynamicRuleEvaluator {

    private final RecommendationsRepository recommendationsRepository;

    public DynamicRuleEvaluator(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    public boolean evaluate(UUID userId, List<Map<String, Object>> rule) {
        for (Map<String, Object> condition : rule) {
            // достаём поля с безопасной проверкой
            String query = (String) condition.get("query");
            List<String> arguments = (List<String>) condition.get("arguments");
            boolean negate = Boolean.TRUE.equals(condition.get("negate"));

            // проверяем null перед использованием
            if (query == null || arguments == null || arguments.isEmpty()) {
                System.out.println("⚠️ Некорректное правило: " + condition);
                return false;
            }

            boolean result = switch (query) {
                case "USER_OF" -> checkUserOf(userId, arguments.get(0), negate);
                case "ACTIVE_USER_OF" -> checkActiveUserOf(userId, arguments.get(0), negate);
                case "TRANSACTION_SUM_COMPARE" ->
                        checkTransactionSumCompare(userId, arguments.get(0), arguments.get(1),
                                arguments.get(2), Integer.parseInt(arguments.get(3)), negate);
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" ->
                        checkTransactionSumDepositWithdraw(userId, arguments.get(0), arguments.get(1), negate);
                default -> {
                    System.out.println("⚠️ Неизвестный query: " + query);
                    yield false;
                }
            };

            if (!result) {
                return false;
            }
        }
        return true;
    }


    private boolean checkUserOf(UUID userId, String productType, boolean negate) {
        boolean result = recommendationsRepository.userHasProductType(userId, productType);
        return negate ? !result : result;
    }

    private boolean checkActiveUserOf(UUID userId, String productType, boolean negate) {
        int count = recommendationsRepository.countTransactionsByType(userId, productType);
        boolean result = count >= 5;
        return negate ? !result : result;
    }

    private boolean checkTransactionSumCompare(UUID userId, String productType, String txType,
                                               String operator, int value, boolean negate) {
        int sum = recommendationsRepository.sumTransactionByType(userId, productType, txType);
        boolean result = switch (operator) {
            case ">" -> sum > value;
            case "<" -> sum < value;
            case "=" -> sum == value;
            case ">=" -> sum >= value;
            case "<=" -> sum <= value;
            default -> false;
        };
        return negate ? !result : result;
    }

    private boolean checkTransactionSumDepositWithdraw(UUID userId, String productType,
                                                       String operator, boolean negate) {
        int depositSum = recommendationsRepository.sumTransactionByType(userId, productType, "DEPOSIT");
        int withdrawSum = recommendationsRepository.sumTransactionByType(userId, productType, "WITHDRAW");
        boolean result = switch (operator) {
            case ">" -> depositSum > withdrawSum;
            case "<" -> depositSum < withdrawSum;
            case "=" -> depositSum == withdrawSum;
            case ">=" -> depositSum >= withdrawSum;
            case "<=" -> depositSum <= withdrawSum;
            default -> false;
        };
        return negate ? !result : result;
    }
}

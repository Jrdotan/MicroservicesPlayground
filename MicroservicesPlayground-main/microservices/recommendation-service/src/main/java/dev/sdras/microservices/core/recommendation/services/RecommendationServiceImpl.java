package dev.sdras.microservices.core.recommendation.services;

import dev.sdras.api.core.recommendation.Recommendation;
import dev.sdras.api.core.recommendation.RecommendationService;
import dev.sdras.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private final Random random = new Random();

    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        LOG.debug("/recommendation return recommendations for productId={}", productId);
        return IntStream.rangeClosed(1, 3)
                .mapToObj(recommendationId -> new Recommendation(
                        productId,
                        recommendationId,
                        "Autor " + recommendationId,
                        random.nextInt(5) + 1,
                        "Análise" + productId + " Pelo autor " + recommendationId,
                        serviceUtil.getServerAddress()))
                .toList();
    }
}

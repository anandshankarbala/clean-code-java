package com.example.CleanCode;

import lombok.extern.java.Log;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import static org.mockito.Mockito.mock;

class Movie{
    Movie(Type type) {
        this.type = type;
    }

    enum Type {
        REGULAR(PriceService::computeRegularPrice),
        NEW_RELEASE(PriceService::computeNewReleasePrice),
        CHILDREN(PriceService::computeChildrenPrice);

        public final BiFunction<PriceService,Integer,Integer> priceAlgo;

        Type(BiFunction<PriceService,Integer,Integer> priceAlgo){
            this.priceAlgo=priceAlgo;
        }

    }
    private final Type type;
}

interface NewReleasePriceRepo {
    double getFactor();     // API call DEFAULT 2
}

@Service
@Log
class PriceService {
    private final NewReleasePriceRepo repo;

    public PriceService(NewReleasePriceRepo repo) {
        this.repo = repo;
    }

    int computeNewReleasePrice(int days){
        return (int) (days * repo.getFactor());
    }

    int computeChildrenPrice(int days){
        return 5;
    }

    int computeRegularPrice(int days){
        return days + 1;
    }

    public int computePrice(Movie.Type type,int days){
        log.info("compute price for the days {} " + days);
        return type.priceAlgo.apply(this,days);
    }
}
@Log
public class EnunMainBiFunction {

    public static void main(String[] args){
        log.info("MAIN CLASS");
        NewReleasePriceRepo repo = mock(NewReleasePriceRepo.class);
        Mockito.when(repo.getFactor()).thenReturn(2d);
        PriceService priceService = new PriceService(repo);
        System.out.println(priceService.computePrice(Movie.Type.CHILDREN,3));
        System.out.println(priceService.computePrice(Movie.Type.REGULAR,3));
        System.out.println(priceService.computePrice(Movie.Type.NEW_RELEASE,3));
    }
}


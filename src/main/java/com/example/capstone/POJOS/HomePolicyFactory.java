package com.example.capstone.POJOS;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Factory class, create HomePolicy objects
 */
public class HomePolicyFactory {
    private HomePolicyFactory(){}

    /**
     * Takes in an homeQuote object then create an HomePolicy object based on the attributes come with the quote
     * @param homeQuote Home Quote
     * @return homePolicy Home Policy
     */
    public static HomePolicy createHomePolicy(HomeQuote homeQuote){
        return new HomePolicy.Builder(homeQuote.getInsuredPerson(), homeQuote.getHome(), homeQuote.getLiabilityLimit(),
                homeQuote.getDeductible(), homeQuote.getContentsInsuranceLimit()
                , homeQuote.getContentsDeductible(), homeQuote.getBasePremium(), homeQuote.getTax(), homeQuote.getTotalPremium()).build();
    }

    /**
     * Takes in an homePolicy object, and checks the end date on the policy
     * if the end date is within 60 days, create a new policy. Otherwise, return
     * the original policy object and inform user
     * @param homePolicy Home Policy
     * @return homePolicy Home Policy
     */
    public static HomePolicy renewHomePolicy(HomePolicy homePolicy){
        if (ChronoUnit.DAYS.between(LocalDate.now(), homePolicy.getEndDate()) <= 60){
            return new HomePolicy.Builder(homePolicy.getInsuredPerson(), homePolicy.getHome(), homePolicy.getLiabilityLimit(),
                    homePolicy.getDeductible(), homePolicy.getContentsInsuranceLimit()
                    , homePolicy.getContentsDeductible(), homePolicy.getBasePremium(), homePolicy.getTax(), homePolicy.getTotalPremium()).build();
        } else {
            return homePolicy;
        }
    }
}


package com.socialmediaraiser.core.twitter.properties;

import com.socialmediaraiser.core.twitter.scoring.Criterion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScoringProperties {

    private List<ScoringProperty> properties;

    public int getTotalMaxPoints(){
        int total = 0;
        for(ScoringProperty property : properties){
            if(property.isActive()){
                total+=property.getMaxPoints();
            }
        }
        return total;
    }

    public ScoringProperty getProperty(Criterion c){
        ScoringProperty result = null;
        for(ScoringProperty sp : properties){
            if(sp.getCriterion() == c){
                result = sp;
                break;
            }
        }
        return result;
    }
}

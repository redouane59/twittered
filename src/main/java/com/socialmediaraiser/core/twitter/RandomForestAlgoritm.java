package com.socialmediaraiser.core.twitter;

import com.socialmediaraiser.core.twitter.helpers.GoogleSheetHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RandomForestAlgoritm {

    private static RandomForest forest;
    private static SecureRandom random = new SecureRandom();
    private static final Logger LOGGER = Logger.getLogger(RandomForestAlgoritm.class.getName());


    private RandomForestAlgoritm() {
        throw new IllegalStateException("Utility class");
    }

    public static void process() throws Exception {

        GoogleSheetHelper sheetHelper = new GoogleSheetHelper(null);
        List<List<Object>> data = sheetHelper.getRandomForestData();

        Instances trainingDataSet = new Instances("trainingDataSet", getAttributes(), 0);
        Instances testingDataSet = new Instances("testingDataSet", getAttributes(), 0);

        for(List<Object> line : data){
            boolean followBack = Boolean.parseBoolean(line.get(7).toString());
            double followBackValue = 0;
            if(followBack) {
                followBackValue = 1;
            }

            double[] attValues = {Double.valueOf(line.get(0).toString()),
                    Double.valueOf(line.get(1).toString()),
                    Double.valueOf(line.get(2).toString()),
                    Double.valueOf(line.get(3).toString()),
                    trainingDataSet.attribute("DateOfFollow").parseDate(line.get(4).toString()),
                    Double.valueOf(line.get(5).toString()),
                    Double.valueOf(line.get(6).toString()),
                    followBackValue};

            if(random.nextFloat()>0.8){
                testingDataSet.add(new DenseInstance(1.0, attValues));
                testingDataSet.setClassIndex(testingDataSet.numAttributes()-1);
            } else{
                trainingDataSet.add(new DenseInstance(1.0, attValues));
                trainingDataSet.setClassIndex(trainingDataSet.numAttributes()-1);
            }

        }

        forest = new RandomForest();
        forest.setNumIterations(100);

        forest.buildClassifier(trainingDataSet);

        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(forest, testingDataSet);
        LOGGER.info(()->"");
    }

    private static ArrayList<Attribute> getAttributes(){
        Attribute followers = new Attribute("Followers");
        Attribute followings = new Attribute("Followings");
        Attribute nbDaySinceLastTweet = new Attribute("NbDaySinceLastTweet");
        Attribute commonFollowers = new Attribute("CommonFollowers");
        Attribute dateOfFollow = new Attribute("DateOfFollow", "yyyy/MM/dd HH:mm");
        Attribute tweets = new Attribute("Tweets");
        Attribute yearsSinceCreation = new Attribute("YearsSinceCreation");
        List<String> fvClassVal = new ArrayList<>(2);
        fvClassVal.add("false");
        fvClassVal.add("true");
        Attribute cClass = new Attribute("FollowBack", fvClassVal);
        ArrayList<Attribute> fvWekaAttributes = new ArrayList();
        fvWekaAttributes.add(followers);
        fvWekaAttributes.add(followings);
        fvWekaAttributes.add(nbDaySinceLastTweet);
        fvWekaAttributes.add(commonFollowers);
        fvWekaAttributes.add(dateOfFollow);
        fvWekaAttributes.add(tweets);
        fvWekaAttributes.add(yearsSinceCreation);
        fvWekaAttributes.add(cClass);
        return fvWekaAttributes;
    }

    public static boolean getPrediction(AbstractUser user) {
         Instances dataset = new Instances("whatever", getAttributes(), 0);
        //Followers	Followings	NbDaySinceLastTweet	CommonFollowers	DateOfFollow	Tweets	YearsSinceCreation	FollowBack
        double[] attValues = new double[0];
        try {
            attValues = new double[]{user.getFollowersCount(),
                    user.getFollowingCount(),
                    user.getDaysBetweenFollowAndLastUpdate()
                    , user.getCommonFollowers(),
                    dataset.attribute(4).parseDate(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(user.getDateOfFollow())),
                    user.getTweetCount(),
                    user.getYearsBetweenFollowAndCreation()};
        } catch (ParseException e) {
            LOGGER.severe(e.getMessage());
        }


        Instance i1 = new DenseInstance(1.0, attValues);
        dataset.add(i1);
        dataset.setClassIndex(dataset.numAttributes()-1); // @todo correct ?

        try {
            double result = forest.classifyInstance(dataset.instance(0));
            return result == 1.0;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return false;
        }
    }

}

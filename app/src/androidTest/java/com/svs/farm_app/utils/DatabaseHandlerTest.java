package com.svs.farm_app.utils;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.svs.farm_app.entities.AssignedInputs;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.entities.CollectedOrder;
import com.svs.farm_app.entities.Companies;
import com.svs.farm_app.entities.CottonDeduction;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.entities.FarmDataColleted;
import com.svs.farm_app.entities.FarmIncome;
import com.svs.farm_app.entities.FarmOtherCrops;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.entities.FingerFive;
import com.svs.farm_app.entities.FingerFour;
import com.svs.farm_app.entities.FingerOne;
import com.svs.farm_app.entities.FingerThree;
import com.svs.farm_app.entities.FingerTwo;
import com.svs.farm_app.entities.FoliarFeed;
import com.svs.farm_app.entities.Germination;
import com.svs.farm_app.entities.Herbicides;
import com.svs.farm_app.entities.MappedFarm;
import com.svs.farm_app.entities.OfficerTraining;
import com.svs.farm_app.entities.OtherCrops;
import com.svs.farm_app.entities.PlantingRains;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.entities.ReRegisteredFarmers;
import com.svs.farm_app.entities.RecoveredCash;
import com.svs.farm_app.entities.Scouting;
import com.svs.farm_app.entities.ShowIntent;
import com.svs.farm_app.entities.SignedDoc;
import com.svs.farm_app.entities.SubVillage;
import com.svs.farm_app.entities.TransportHseToMarket;
import com.svs.farm_app.entities.UserVillage;
import com.svs.farm_app.entities.Users;
import com.svs.farm_app.entities.Village;
import com.svs.farm_app.entities.WackFarmer;
import com.svs.farm_app.entities.YieldEstimate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Wamae on 14-Mar-17.
 */
//@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
public class DatabaseHandlerTest {

    private static final String TAG = DatabaseHandlerTest.class.getSimpleName();

    //@Mock
    //Context mockContext;

    private DatabaseHandler db;
    private RandomObjectFiller randomObjectFiller;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        randomObjectFiller = new RandomObjectFiller();
        db = new DatabaseHandler(appContext);
    }

    @After
    public void tearDown() {
        db.close();
    }

   /* @AfterClass
    public static void tearDownClass(){
        //db.close();
    }*/

    @Test
    public void addRegisteredFarmers() throws Exception {

    }

    @Test
    public void addGermination() throws Exception {
        db.clearTable(db.TABLE_GERMINATION);

        Germination germination = randomObjectFiller.createAndFill(Germination.class);
        db.addGermination(germination);

        List<Germination> germinationList = db.getAllGerminations();

        assertThat(germination.getCompanyId(), is(equalTo(germinationList.get(0).getCompanyId())));
        assertThat(germination.getLatitude(), is(equalTo(germinationList.get(0).getLatitude())));
        assertThat(germination.getLongitude(), is(equalTo(germinationList.get(0).getLongitude())));
        assertThat(germination.getCollectDate(), is(equalTo(germinationList.get(0).getCollectDate())));
        assertThat(germination.getFarmId(), is(equalTo(germinationList.get(0).getFarmId())));
        assertThat(germination.getGerminationDate(), is(equalTo(germinationList.get(0).getGerminationDate())));
        assertThat(germination.getRemarks(), is(equalTo(germinationList.get(0).getRemarks())));
        assertThat(germination.getUserId(), is(equalTo(germinationList.get(0).getUserId())));

        db.clearTable(db.TABLE_GERMINATION);
    }

    @Test
    public void addPlantingRains() throws Exception {

        db.clearTable(db.TABLE_PLANTING_RAINS);

        PlantingRains plantingRains = randomObjectFiller.createAndFill(PlantingRains.class);
        db.addPlantingRains(plantingRains);

        List<PlantingRains> plantingRainsList = db.getAllPlantingRains();

        assertThat(plantingRains.getCompanyID(), is(equalTo(plantingRainsList.get(0).getCompanyID())));
        assertThat(plantingRains.getLatitude(), is(equalTo(plantingRainsList.get(0).getLatitude())));
        assertThat(plantingRains.getLongitude(), is(equalTo(plantingRainsList.get(0).getLongitude())));
        assertThat(plantingRains.getCollectDate(), is(equalTo(plantingRainsList.get(0).getCollectDate())));
        assertThat(plantingRains.getFarmID(), is(equalTo(plantingRainsList.get(0).getFarmID())));
        assertThat(plantingRains.getRemarks(), is(equalTo(plantingRainsList.get(0).getRemarks())));
        assertThat(plantingRains.getUserID(), is(equalTo(plantingRainsList.get(0).getUserID())));

        db.clearTable(db.TABLE_PLANTING_RAINS);

    }

    @Test
    public void addFarmOtherCrops() throws Exception {
        db.clearTable(db.TABLE_FARM_OTHER_CROPS);

        FarmOtherCrops farmOtherCrops = randomObjectFiller.createAndFill(FarmOtherCrops.class);
        db.addFarmOtherCrops(farmOtherCrops);

        List<FarmOtherCrops> farmOtherCropsList = db.getAllFarmOtherCrops();

        assertThat(farmOtherCrops.getCompanyId(), is(equalTo(farmOtherCropsList.get(0).getCompanyId())));
        assertThat(farmOtherCrops.getLatitude(), is(equalTo(farmOtherCropsList.get(0).getLatitude())));
        assertThat(farmOtherCrops.getLongitude(), is(equalTo(farmOtherCropsList.get(0).getLongitude())));
        assertThat(farmOtherCrops.getCollectDate(), is(equalTo(farmOtherCropsList.get(0).getCollectDate())));
        assertThat(farmOtherCrops.getFarmID(), is(equalTo(farmOtherCropsList.get(0).getFarmID())));
        assertThat(farmOtherCrops.getUserId(), is(equalTo(farmOtherCropsList.get(0).getUserId())));
        assertThat(farmOtherCrops.getCropIdOne(), is(equalTo(farmOtherCropsList.get(0).getCropIdOne())));
        assertThat(farmOtherCrops.getCropIdTwo(), is(equalTo(farmOtherCropsList.get(0).getCropIdTwo())));
        assertThat(farmOtherCrops.getCropIdThree(), is(equalTo(farmOtherCropsList.get(0).getCropIdThree())));

        db.clearTable(db.TABLE_FARM_OTHER_CROPS);
    }

    @Test
    public void addOtherCrops() throws Exception {
        db.clearTable(db.TABLE_OTHER_CROPS);

        OtherCrops otherCrops = randomObjectFiller.createAndFill(OtherCrops.class);
        db.addOtherCrops(otherCrops);

        List<OtherCrops> otherCropsList = db.allOtherCrops();

        assertThat(otherCrops.getCropID(), is(equalTo(otherCropsList.get(0).getCropID())));
        assertThat(otherCrops.getCropName(), is(equalTo(otherCropsList.get(0).getCropName())));

        db.clearTable(db.TABLE_OTHER_CROPS);
    }

    @Test
    public void addHerbicides() throws Exception {
        db.clearTable(db.TABLE_HERBICIDES);

        Herbicides herbicides = randomObjectFiller.createAndFill(Herbicides.class);
        db.addHerbicides(herbicides);

        List<Herbicides> otherCropsList = db.getAllHerbicides();

        assertThat(herbicides.getInputID(), is(equalTo(otherCropsList.get(0).getInputID())));
        assertThat(herbicides.getInputType(), is(equalTo(otherCropsList.get(0).getInputType())));

        db.clearTable(db.TABLE_HERBICIDES);
    }

    @Test
    public void addFoliarFeed() throws Exception {
        db.clearTable(db.TABLE_FOLIAR_FEED);

        FoliarFeed foliarFeed = randomObjectFiller.createAndFill(FoliarFeed.class);
        db.addFoliarFeed(foliarFeed);

        List<FoliarFeed> foliarFeedList = db.getAllFoliarFeed();

        assertThat(foliarFeed.getInputID(), is(equalTo(foliarFeedList.get(0).getInputID())));
        assertThat(foliarFeed.getInputType(), is(equalTo(foliarFeedList.get(0).getInputType())));

        db.clearTable(db.TABLE_FOLIAR_FEED);
    }

    @Test
    public void addFarmAssMajor() throws Exception {
        db.clearTable(db.TABLE_FARM_ASS_MAJOR);

        FarmAssFormsMajor formsMajor = randomObjectFiller.createAndFill(FarmAssFormsMajor.class);
        db.addFarmAssMajor(formsMajor);

        List<FarmAssFormsMajor> formsMajorList = db.getAllFormsMajor();

        // assertThat(formsMajor.getInputId(), is(equalTo(formsMajorList.get(0).getInputId())));
        assertThat(formsMajor.getUserId(), is(equalTo(formsMajorList.get(0).getUserId())));
        assertThat(formsMajor.getRemarks(), is(equalTo(formsMajorList.get(0).getRemarks())));
        assertThat(formsMajor.getFarmId(), is(equalTo(formsMajorList.get(0).getFarmId())));
        assertThat(formsMajor.getActivityDate(), is(equalTo(formsMajorList.get(0).getActivityDate())));
        assertThat(formsMajor.getActivityMethod(), is(equalTo(formsMajorList.get(0).getActivityMethod())));
        assertThat(formsMajor.getCompanyId(), is(equalTo(formsMajorList.get(0).getCompanyId())));
        assertThat(formsMajor.getCollectDate(), is(equalTo(formsMajorList.get(0).getCollectDate())));
        assertThat(formsMajor.getFamilyHours(), is(equalTo(formsMajorList.get(0).getFamilyHours())));
        assertThat(formsMajor.getFormTypeId(), is(equalTo(formsMajorList.get(0).getFormTypeId())));
        assertThat(formsMajor.getHiredHours(), is(equalTo(formsMajorList.get(0).getHiredHours())));
        //assertThat(formsMajor.getInputQuantity(), is(equalTo(formsMajorList.get(0).getInputQuantity())));
        assertThat(formsMajor.getLatitude(), is(equalTo(formsMajorList.get(0).getLatitude())));
        assertThat(formsMajor.getLongitude(), is(equalTo(formsMajorList.get(0).getLongitude())));
        assertThat(formsMajor.getMoneyOut(), is(equalTo(formsMajorList.get(0).getMoneyOut())));
        //assertThat(formsMajor.getSprayerType(), is(equalTo(formsMajorList.get(0).getSprayerType())));

        db.clearTable(db.TABLE_FARM_ASS_MAJOR);
    }

    @Test
    public void addUserVillage() throws Exception {
        db.clearTable(db.TABLE_USER_VILLAGE);

        UserVillage userVillage = randomObjectFiller.createAndFill(UserVillage.class);
        db.addUserVillage(userVillage);

        List<UserVillage> userVillageList = db.getAllUserVillages();

        assertThat(userVillage.getUserId(), is(equalTo(userVillageList.get(0).getUserId())));
        assertThat(userVillage.getVillageId(), is(equalTo(userVillageList.get(0).getVillageId())));

        db.clearTable(db.TABLE_USER_VILLAGE);
    }

    @Test
    public void addUser() throws Exception {
        db.clearTable(db.TABLE_USERS);

        Users users = randomObjectFiller.createAndFill(Users.class);
        db.addUser(users);

        List<Users> allUsers = db.getAllUsers();

        assertThat(users.getUserID(), is(equalTo(allUsers.get(0).getUserID())));
        assertThat(users.getCompanyID(), is(equalTo(allUsers.get(0).getCompanyID())));
        assertThat(users.getUserName(), is(equalTo(allUsers.get(0).getUserName())));
        assertThat(users.getPassword(), is(equalTo(allUsers.get(0).getPassword())));

        db.clearTable(db.TABLE_USERS);
    }

    @Test
    public void addAssignedInput() throws Exception {
        db.clearTable(db.TABLE_ASSIGNED_INPUTS);

        AssignedInputs assignedInputs = randomObjectFiller.createAndFill(AssignedInputs.class);
        db.addAssignedInput(assignedInputs);

        List<AssignedInputs> assignedInputsList = db.getAllAssignedInputs();

        assertThat(assignedInputs.getFarmerId(), is(equalTo(assignedInputsList.get(0).getFarmerId())));
        assertThat(assignedInputs.getCardNo(), is(equalTo(assignedInputsList.get(0).getCardNo())));
        assertThat(assignedInputs.getInputAssInputId(), is(equalTo(assignedInputsList.get(0).getInputAssInputId())));
        assertThat(assignedInputs.getInputId(), is(equalTo(assignedInputsList.get(0).getInputId())));
        assertThat(assignedInputs.getInputQuantity(), is(equalTo(assignedInputsList.get(0).getInputQuantity())));
        assertThat(assignedInputs.getInputTotal(), is(equalTo(assignedInputsList.get(0).getInputTotal())));
        assertThat(assignedInputs.getInputType(), is(equalTo(assignedInputsList.get(0).getInputType())));
        assertThat(assignedInputs.getOrderId(), is(equalTo(assignedInputsList.get(0).getOrderId())));
        assertThat(assignedInputs.getOrderNum(), is(equalTo(assignedInputsList.get(0).getOrderNum())));

        db.clearTable(db.TABLE_ASSIGNED_INPUTS);
    }

    @Test
    public void addVillages() throws Exception {
        db.clearTable(db.TABLE_VILLAGES);

        Village village = randomObjectFiller.createAndFill(Village.class);
        db.addVillages(village);

        List<Village> villageList = db.getVillages();

        assertThat(village.getVillageID(), is(equalTo(villageList.get(0).getVillageID())));
        assertThat(village.getVillageName(), is(equalTo(villageList.get(0).getVillageName())));
        //assertThat(villages.getTimes(), is(equalTo(villagesList.get(0).getTimes())));

        db.clearTable(db.TABLE_VILLAGES);
    }

    @Test
    public void addSubVillages() throws Exception {
        db.clearTable(db.TABLE_SUBVILLAGES);

        SubVillage subVillage = randomObjectFiller.createAndFill(SubVillage.class);
        db.addSubvillages(subVillage);

        List<SubVillage> villagesList = db.getAllSubVillages();

        assertThat(subVillage.getVillageID(), is(equalTo(villagesList.get(0).getVillageID())));
        assertThat(subVillage.getSubVillageID(), is(equalTo(villagesList.get(0).getSubVillageID())));
        assertThat(subVillage.getSubVillageName(), is(equalTo(villagesList.get(0).getSubVillageName())));

        db.clearTable(db.TABLE_SUBVILLAGES);
    }

   /* @Test
    public void addInput() throws Exception {
        db.clearTable(db.TABLE_INP);

        SubVillages subVillages = randomObjectFiller.createAndFill(SubVillages.class);
        db.addSubvillages(subVillages);

        List<SubVillages> villagesList = db.getAllSubVillages();

        assertThat(subVillages.getVillageId(), is(equalTo(villagesList.get(0).getVillageId())));
        assertThat(subVillages.getSubVillageID(), is(equalTo(villagesList.get(0).getSubVillageID())));
        assertThat(subVillages.getSubVillageName(), is(equalTo(villagesList.get(0).getSubVillageName())));

        db.clearTable(db.TABLE_SUBVILLAGES);
    }*/

    @Test
    public void addFarm() throws Exception {
        db.clearTable(db.TABLE_FARMS);

        Farm farm = randomObjectFiller.createAndFill(Farm.class);
        db.addFarm(farm);

        List<Farm> villagesList = db.getAllFarms();

        assertThat(farm.getFarmID(), is(equalTo(villagesList.get(0).getFarmID())));
        assertThat(farm.getFarmName(), is(equalTo(villagesList.get(0).getFarmName())));
        assertThat(farm.getFarmPerimeter(), is(equalTo(villagesList.get(0).getFarmPerimeter())));
        assertThat(farm.getActualFarmArea(), is(equalTo(villagesList.get(0).getActualFarmArea())));
        assertThat(farm.getEstimatedFarmArea(), is(equalTo(villagesList.get(0).getEstimatedFarmArea())));
        assertThat(farm.getVillageID(), is(equalTo(villagesList.get(0).getVillageID())));
        assertThat(farm.getFarmerID(), is(equalTo(villagesList.get(0).getFarmerID())));
        assertThat(farm.getCompanyID(), is(equalTo(villagesList.get(0).getCompanyID())));
        assertThat(farm.getFarmAss(), is(equalTo(villagesList.get(0).getFarmAss())));
        assertThat(farm.getLatitude(), is(equalTo(villagesList.get(0).getLatitude())));
        assertThat(farm.getLongitude(), is(equalTo(villagesList.get(0).getLongitude())));


        db.clearTable(db.TABLE_FARMS);
    }

    @Test
    public void addFingerOne() throws Exception {
        db.clearTable(db.TABLE_FINGER_ONE);

        FingerOne fingerOne = randomObjectFiller.createAndFill(FingerOne.class);
        db.addFingerOne(fingerOne);

        List<FingerOne> fingerOneList = db.getAllFingerOne();

        assertThat(fingerOne.getFarmId(), is(equalTo(fingerOneList.get(0).getFarmId())));
        assertThat(fingerOne.getCompanyId(), is(equalTo(fingerOneList.get(0).getCompanyId())));
        assertThat(fingerOne.getCropResidues(), is(equalTo(fingerOneList.get(0).getCropResidues())));
        assertThat(fingerOne.getCropRotation(), is(equalTo(fingerOneList.get(0).getCropRotation())));
        assertThat(fingerOne.getErosionPrevention(), is(equalTo(fingerOneList.get(0).getErosionPrevention())));
        //assertThat(fingerOne.getCollectionDate(), is(equalTo(fingerOneList.get(0).getCollectionDate())));
        assertThat(fingerOne.getLandPreparation(), is(equalTo(fingerOneList.get(0).getLandPreparation())));
        assertThat(fingerOne.getManure(), is(equalTo(fingerOneList.get(0).getManure())));
        assertThat(fingerOne.getRatoon(), is(equalTo(fingerOneList.get(0).getRatoon())));
        assertThat(fingerOne.getSeedBedPreparation(), is(equalTo(fingerOneList.get(0).getSeedBedPreparation())));
        assertThat(fingerOne.getSoilType(), is(equalTo(fingerOneList.get(0).getSoilType())));
        assertThat(fingerOne.getUserId(), is(equalTo(fingerOneList.get(0).getUserId())));
        assertThat(fingerOne.getWaterLogRisk(), is(equalTo(fingerOneList.get(0).getWaterLogRisk())));

        db.clearTable(db.TABLE_FINGER_ONE);
    }

    @Test
    public void addFingerTwo() throws Exception {
        db.clearTable(db.TABLE_FINGER_TWO);

        FingerTwo fingerTwo = randomObjectFiller.createAndFill(FingerTwo.class);
        db.addFingerTwo(fingerTwo);

        List<FingerTwo> allFingerTwo = db.getAllFingerTwo();

        assertThat(fingerTwo.getFarmId(), is(equalTo(allFingerTwo.get(0).getFarmId())));
        assertThat(fingerTwo.getCompanyId(), is(equalTo(allFingerTwo.get(0).getCompanyId())));
        assertThat(fingerTwo.getCorrectSeedPlanting(), is(equalTo(allFingerTwo.get(0).getCorrectSeedPlanting())));
        assertThat(fingerTwo.getPlantingTime(), is(equalTo(allFingerTwo.get(0).getPlantingTime())));
        assertThat(fingerTwo.getRowSpacing(), is(equalTo(allFingerTwo.get(0).getRowSpacing())));
        assertThat(fingerTwo.getSeedsPerStation(), is(equalTo(allFingerTwo.get(0).getSeedsPerStation())));
        assertThat(fingerTwo.getUserId(), is(equalTo(allFingerTwo.get(0).getUserId())));

        db.clearTable(db.TABLE_FINGER_TWO);
    }

    @Test
    public void addFingerThree() throws Exception {

        db.clearTable(db.TABLE_FINGER_THREE);

        FingerThree fingerThree = randomObjectFiller.createAndFill(FingerThree.class);
        db.addFingerThree(fingerThree);

        List<FingerThree> allFingerTwo = db.getAllFingerThree();

        assertThat(fingerThree.getFarmId(), is(equalTo(allFingerTwo.get(0).getFarmId())));
        assertThat(fingerThree.getCompanyId(), is(equalTo(allFingerTwo.get(0).getCompanyId())));
        assertThat(fingerThree.getFillAfterEmergence(), is(equalTo(allFingerTwo.get(0).getFillAfterEmergence())));
        //assertThat(fingerThree.getCollectionDate(), is(equalTo(allFingerTwo.get(0).getCollectionDate())));
        assertThat(fingerThree.getGapFilling(), is(equalTo(allFingerTwo.get(0).getGapFilling())));
        assertThat(fingerThree.getRecommendedPlantsPerStation(), is(equalTo(allFingerTwo.get(0).getRecommendedPlantsPerStation())));
        assertThat(fingerThree.getThinningAfterEmergence(), is(equalTo(allFingerTwo.get(0).getThinningAfterEmergence())));
        assertThat(fingerThree.getUserId(), is(equalTo(allFingerTwo.get(0).getUserId())));

        db.clearTable(db.TABLE_FINGER_THREE);
    }

    @Test
    public void addFingerFour() throws Exception {
        db.clearTable(db.TABLE_FINGER_FOUR);

        FingerFour fingerFour = randomObjectFiller.createAndFill(FingerFour.class);
        db.addFingerFour(fingerFour);

        List<FingerFour> allFingerFour = db.getAllFingerFour();

        assertThat(fingerFour.getFarmId(), is(equalTo(allFingerFour.get(0).getFarmId())));
        assertThat(fingerFour.getFirstBranch(), is(equalTo(allFingerFour.get(0).getFirstBranch())));
        assertThat(fingerFour.getFoliar(), is(equalTo(allFingerFour.get(0).getFoliar())));
        assertThat(fingerFour.getWeeds(), is(equalTo(allFingerFour.get(0).getWeeds())));
        //assertThat(fingerFour.getCollectionDate(), is(equalTo(allFingerFour.get(0).getCollectionDate())));
        //assertThat(fingerFour.getLatitude(), is(equalTo(allFingerFour.get(0).getRecommendedPlantsPerStation())));
        //assertThat(fingerFour.getLongitude(), is(equalTo(allFingerFour.get(0).getThinningAfterEmergence())));
        assertThat(fingerFour.getUserId(), is(equalTo(allFingerFour.get(0).getUserId())));

        db.clearTable(db.TABLE_FINGER_FOUR);
    }

    @Test
    public void addFingerFive() throws Exception {
        db.clearTable(db.TABLE_FINGER_FIVE);

        FingerFive fingerFive = randomObjectFiller.createAndFill(FingerFive.class);
        db.addFingerFive(fingerFive);

        List<FingerFive> allFingerFive = db.getAllFingerFive();

        assertThat(fingerFive.getFarmId(), is(equalTo(allFingerFive.get(0).getFarmId())));
        assertThat(fingerFive.getCompanyId(), is(equalTo(allFingerFive.get(0).getCompanyId())));
        //assertThat(fingerFive.getCollectionDate(), is(equalTo(allFingerFive.get(0).getCollectionDate())));
        assertThat(fingerFive.getPestLevel(), is(equalTo(allFingerFive.get(0).getPestLevel())));
        assertThat(fingerFive.getCorrectPesticideUse(), is(equalTo(allFingerFive.get(0).getCorrectPesticideUse())));
        assertThat(fingerFive.getIfEmptyCansOnFarm(), is(equalTo(allFingerFive.get(0).getIfEmptyCansOnFarm())));
        assertThat(fingerFive.getLastScout(), is(equalTo(allFingerFive.get(0).getLastScout())));
        assertThat(fingerFive.getPegBoardAvailability(), is(equalTo(allFingerFive.get(0).getPegBoardAvailability())));
        assertThat(fingerFive.getPestAbsenceDuration(), is(equalTo(allFingerFive.get(0).getPestAbsenceDuration())));
        assertThat(fingerFive.getSafeUsageCans(), is(equalTo(allFingerFive.get(0).getSafeUsageCans())));
        assertThat(fingerFive.getScoutMethod(), is(equalTo(allFingerFive.get(0).getScoutMethod())));
        assertThat(fingerFive.getSprayTime(), is(equalTo(allFingerFive.get(0).getSprayTime())));
        assertThat(fingerFive.getUserId(), is(equalTo(allFingerFive.get(0).getUserId())));
        //assertThat(fingerFour.getLatitude(), is(equalTo(allFingerFour.get(0).getRecommendedPlantsPerStation())));
        //assertThat(fingerFour.getLongitude(), is(equalTo(allFingerFour.get(0).getThinningAfterEmergence())));

        db.clearTable(db.TABLE_FINGER_FIVE);
    }

    @Test
    public void addSignedDoc() throws Exception {
        db.clearTable(db.TABLE_SIGNED_DOCS);

        SignedDoc signedDoc = randomObjectFiller.createAndFill(SignedDoc.class);
        db.addSignedDoc(signedDoc);

        List<SignedDoc> allSignedDocs = db.getAllSignedDocs();

        assertThat(signedDoc.getFarmerId(), is(equalTo(allSignedDocs.get(0).getFarmerId())));
        assertThat(signedDoc.getCompanyId(), is(equalTo(allSignedDocs.get(0).getCompanyId())));
        assertThat(signedDoc.getUserId(), is(equalTo(allSignedDocs.get(0).getUserId())));

        db.clearTable(db.TABLE_SIGNED_DOCS);
    }

    @Test
    public void addFarmAssMedium() throws Exception {
        db.clearTable(db.TABLE_FARM_ASS_MEDIUM);

        FarmAssFormsMedium farmAssFormsMedium = randomObjectFiller.createAndFill(FarmAssFormsMedium.class);
        db.addFarmAssMedium(farmAssFormsMedium);

        List<FarmAssFormsMedium> farmAssFormsMediumList = db.getAllFormsMedium();

        assertThat(farmAssFormsMedium.getFarmId(), is(equalTo(farmAssFormsMediumList.get(0).getFarmId())));
        assertThat(farmAssFormsMedium.getFormTypeId(), is(equalTo(farmAssFormsMediumList.get(0).getFormTypeId())));
        assertThat(farmAssFormsMedium.getActivityDate(), is(equalTo(farmAssFormsMediumList.get(0).getActivityDate())));
        assertThat(farmAssFormsMedium.getActivityMethod(), is(equalTo(farmAssFormsMediumList.get(0).getActivityMethod())));
        assertThat(farmAssFormsMedium.getCompanyId(), is(equalTo(farmAssFormsMediumList.get(0).getCompanyId())));
        assertThat(farmAssFormsMedium.getCollectDate(), is(equalTo(farmAssFormsMediumList.get(0).getCollectDate())));
        assertThat(farmAssFormsMedium.getFamilyHours(), is(equalTo(farmAssFormsMediumList.get(0).getFamilyHours())));
        assertThat(farmAssFormsMedium.getHiredHours(), is(equalTo(farmAssFormsMediumList.get(0).getHiredHours())));
        assertThat(farmAssFormsMedium.getInputId(), is(equalTo(farmAssFormsMediumList.get(0).getInputId())));
        assertThat(farmAssFormsMedium.getInputQuantity(), is(equalTo(farmAssFormsMediumList.get(0).getInputQuantity())));
        assertThat(farmAssFormsMedium.getLatitude(), is(equalTo(farmAssFormsMediumList.get(0).getLatitude())));
        assertThat(farmAssFormsMedium.getLongitude(), is(equalTo(farmAssFormsMediumList.get(0).getLongitude())));
        assertThat(farmAssFormsMedium.getMoneyOut(), is(equalTo(farmAssFormsMediumList.get(0).getMoneyOut())));
        assertThat(farmAssFormsMedium.getSprayType(), is(equalTo(farmAssFormsMediumList.get(0).getSprayType())));
        assertThat(farmAssFormsMedium.getUserId(), is(equalTo(farmAssFormsMediumList.get(0).getUserId())));

        db.clearTable(db.TABLE_FARM_ASS_MEDIUM);
    }

    @Test
    public void addScouting() throws Exception {
        db.clearTable(db.TABLE_SCOUTING);

        Scouting scouting = randomObjectFiller.createAndFill(Scouting.class);
        db.addScouting(scouting);

        List<Scouting> scoutingList = db.getAllScoutings();

        assertThat(scouting.getFarmId(), is(equalTo(scoutingList.get(0).getFarmId())));
        assertThat(scouting.getAphid(), is(equalTo(scoutingList.get(0).getAphid())));
        assertThat(scouting.getStainer(), is(equalTo(scoutingList.get(0).getStainer())));
        assertThat(scouting.getBeneficialInsects(), is(equalTo(scoutingList.get(0).getBeneficialInsects())));
        assertThat(scouting.getBollWorm(), is(equalTo(scoutingList.get(0).getBollWorm())));
        assertThat(scouting.getJassid(), is(equalTo(scoutingList.get(0).getJassid())));
        assertThat(scouting.getActivityDate(), is(equalTo(scoutingList.get(0).getActivityDate())));
        assertThat(scouting.getCompanyId(), is(equalTo(scoutingList.get(0).getCompanyId())));
        assertThat(scouting.getCollectDate(), is(equalTo(scoutingList.get(0).getCollectDate())));
        assertThat(scouting.getFamilyHours(), is(equalTo(scoutingList.get(0).getFamilyHours())));
        assertThat(scouting.getHiredHours(), is(equalTo(scoutingList.get(0).getHiredHours())));
        assertThat(scouting.getLatitude(), is(equalTo(scoutingList.get(0).getLatitude())));
        assertThat(scouting.getLongitude(), is(equalTo(scoutingList.get(0).getLongitude())));
        assertThat(scouting.getMoneyOut(), is(equalTo(scoutingList.get(0).getMoneyOut())));
        assertThat(scouting.getUserId(), is(equalTo(scoutingList.get(0).getUserId())));
        assertThat(scouting.getSprayDecision(), is(equalTo(scoutingList.get(0).getSprayDecision())));


        db.clearTable(db.TABLE_SCOUTING);
    }

    /*@Test
    public void addTransportModes() throws Exception {

    }*/

    @Test
    public void addTransportHseToMarket() throws Exception {
        db.clearTable(db.TABLE_TRANS_HSE_TO_MARKET);

        TransportHseToMarket transportHseToMarket = randomObjectFiller.createAndFill(TransportHseToMarket.class);
        db.addTransportHseToMarket(transportHseToMarket);

        List<TransportHseToMarket> allTransPortHseToMarket = db.getAllTransPortHseToMarket();

        assertThat(transportHseToMarket.getUserId(), is(equalTo(allTransPortHseToMarket.get(0).getUserId())));
        assertThat(transportHseToMarket.getMoneyOut(), is(equalTo(allTransPortHseToMarket.get(0).getMoneyOut())));
        assertThat(transportHseToMarket.getLatitude(), is(equalTo(allTransPortHseToMarket.get(0).getLatitude())));
        assertThat(transportHseToMarket.getLongitude(), is(equalTo(allTransPortHseToMarket.get(0).getLongitude())));
        assertThat(transportHseToMarket.getCompanyId(), is(equalTo(allTransPortHseToMarket.get(0).getCompanyId())));
        assertThat(transportHseToMarket.getCollectDate(), is(equalTo(allTransPortHseToMarket.get(0).getCollectDate())));
        assertThat(transportHseToMarket.getFarmId(), is(equalTo(allTransPortHseToMarket.get(0).getFarmId())));
        assertThat(transportHseToMarket.getTransportCount(), is(equalTo(allTransPortHseToMarket.get(0).getTransportCount())));
        assertThat(transportHseToMarket.getDeliveryDate(), is(equalTo(allTransPortHseToMarket.get(0).getDeliveryDate())));

        db.clearTable(db.TABLE_TRANS_HSE_TO_MARKET);
    }

    @Test
    public void addPicking() throws Exception {

    }

    @Test
    public void addYieldEstimate() throws Exception {
        db.clearTable(db.TABLE_YIELD_ESTIMATE);

        YieldEstimate yieldEstimate = randomObjectFiller.createAndFill(YieldEstimate.class);
        db.addYieldEstimate(yieldEstimate);

        List<YieldEstimate> allFarmIncome = db.getAllYieldEstimates();

        assertThat(yieldEstimate.getCompanyId(), is(equalTo(allFarmIncome.get(0).getCompanyId())));
        assertThat(yieldEstimate.getFarmId(), is(equalTo(allFarmIncome.get(0).getFarmId())));
        assertThat(yieldEstimate.getDistanceToLeft(), is(equalTo(allFarmIncome.get(0).getDistanceToLeft())));
        assertThat(yieldEstimate.getDistanceToRight(), is(equalTo(allFarmIncome.get(0).getDistanceToRight())));
        assertThat(yieldEstimate.getNumOfBolls(), is(equalTo(allFarmIncome.get(0).getNumOfBolls())));
        assertThat(yieldEstimate.getNumOfPlants(), is(equalTo(allFarmIncome.get(0).getNumOfPlants())));
        assertThat(yieldEstimate.getSamplingStation(), is(equalTo(allFarmIncome.get(0).getSamplingStation())));
        assertThat(yieldEstimate.getUserId(), is(equalTo(allFarmIncome.get(0).getUserId())));
        assertThat(yieldEstimate.getLatitude(), is(equalTo(allFarmIncome.get(0).getLatitude())));
        assertThat(yieldEstimate.getLongitude(), is(equalTo(allFarmIncome.get(0).getLongitude())));
        assertThat(yieldEstimate.getCollectDate(), is(equalTo(allFarmIncome.get(0).getCollectDate())));

        db.clearTable(db.TABLE_YIELD_ESTIMATE);
    }

    @Test
    public void addFarmIncome() throws Exception {
        db.clearTable(db.TABLE_FARM_INCOME);

        FarmIncome farmIncome = randomObjectFiller.createAndFill(FarmIncome.class);
        db.addFarmIncome(farmIncome);

        List<FarmIncome> allFarmIncome = db.getAllFarmIncome();

        assertThat(farmIncome.getUserId(), is(equalTo(allFarmIncome.get(0).getUserId())));
        assertThat(farmIncome.getCompanyId(), is(equalTo(allFarmIncome.get(0).getCompanyId())));
        assertThat(farmIncome.getDeliveryCount(), is(equalTo(allFarmIncome.get(0).getDeliveryCount())));
        assertThat(farmIncome.getDeliveryDate(), is(equalTo(allFarmIncome.get(0).getDeliveryDate())));
        assertThat(farmIncome.getFarmId(), is(equalTo(allFarmIncome.get(0).getFarmId())));
        assertThat(farmIncome.getGradeA(), is(equalTo(allFarmIncome.get(0).getGradeA())));
        assertThat(farmIncome.getGradeB(), is(equalTo(allFarmIncome.get(0).getGradeB())));
        assertThat(farmIncome.getGradeC(), is(equalTo(allFarmIncome.get(0).getGradeC())));
        assertThat(farmIncome.getLatitude(), is(equalTo(allFarmIncome.get(0).getLatitude())));
        assertThat(farmIncome.getLongitude(), is(equalTo(allFarmIncome.get(0).getLongitude())));
        assertThat(farmIncome.getCollectDate(), is(equalTo(allFarmIncome.get(0).getCollectDate())));

        db.clearTable(db.TABLE_FARM_INCOME);
    }

    @Test
    public void addCollectedInputs() throws Exception {
        db.clearTable(db.TABLE_COLLECTED_INPUTS);

        CollectedInputs collectedInputs = randomObjectFiller.createAndFill(CollectedInputs.class);
        db.addCollectedInputs(collectedInputs);

        List<CollectedInputs> allCollectedInputs = db.getAllCollectedInputs();

        assertThat(collectedInputs.getUserId(), is(equalTo(allCollectedInputs.get(0).getUserId())));
        assertThat(collectedInputs.getOrderId(), is(equalTo(allCollectedInputs.get(0).getOrderId())));
        assertThat(collectedInputs.getCollectionMethod(), is(equalTo(allCollectedInputs.get(0).getCollectionMethod())));
        assertThat(collectedInputs.getCollectDate(), is(equalTo(allCollectedInputs.get(0).getCollectDate())));

        db.clearTable(db.TABLE_COLLECTED_INPUTS);
    }

    @Test
    public void addShowIntent() throws Exception {
        db.clearTable(db.TABLE_SHOW_INTENT);

        ShowIntent showIntent = randomObjectFiller.createAndFill(ShowIntent.class);
        db.addShowIntent(showIntent);

        List<ShowIntent> showIntentList = db.getAllShowIntents();

        assertThat(showIntent.getFarmerId(), is(equalTo(showIntentList.get(0).getFarmerId())));
        assertThat(showIntent.getCompanyId(), is(equalTo(showIntentList.get(0).getCompanyId())));
        assertThat(showIntent.getUserId(), is(equalTo(showIntentList.get(0).getUserId())));

        db.clearTable(db.TABLE_SHOW_INTENT);
    }

    @Test
    public void addWackFarmers() throws Exception {
        db.clearTable(db.TABLE_WACK_FARMERS);

        WackFarmer wackFarmer = randomObjectFiller.createAndFill(WackFarmer.class);
        db.addWackFarmers(wackFarmer);

        List<WackFarmer> farmers = db.getWackFarmers();

        assertThat(wackFarmer.getFarmerId(), is(equalTo(farmers.get(0).getFarmerId())));
        assertThat(wackFarmer.getFirstName(), is(equalTo(farmers.get(0).getFirstName())));
        assertThat(wackFarmer.getLastName(), is(equalTo(farmers.get(0).getLastName())));
        assertThat(wackFarmer.getVillageId(), is(equalTo(farmers.get(0).getVillageId())));
        assertThat(wackFarmer.getGenId(), is(equalTo(farmers.get(0).getGenId())));
        assertThat(wackFarmer.getLastName(), is(equalTo(farmers.get(0).getLastName())));
        assertThat(wackFarmer.getWackStatus(), is(equalTo(farmers.get(0).getWackStatus())));

        db.clearTable(db.TABLE_WACK_FARMERS);
    }

    @Test
    public void addReRegisteredFarmer() throws Exception {
        db.clearTable(db.TABLE_RE_REGISTERED_FARMERS);

        ReRegisteredFarmers reRegisteredFarmers = randomObjectFiller.createAndFill(ReRegisteredFarmers.class);
        db.addReRegisteredFarmer(reRegisteredFarmers);

        List<ReRegisteredFarmers> reRegisteredFarmersList = db.getReRegisteredFarmers();

        assertThat(reRegisteredFarmers.getFarmerId(), is(equalTo(reRegisteredFarmersList.get(0).getFarmerId())));
        assertThat(reRegisteredFarmers.getFarmerPic(), is(equalTo(reRegisteredFarmersList.get(0).getFarmerPic())));
        assertThat(reRegisteredFarmers.getGenId(), is(equalTo(reRegisteredFarmersList.get(0).getGenId())));
        assertThat(reRegisteredFarmers.getLeftThumb(), is(equalTo(reRegisteredFarmersList.get(0).getLeftThumb())));
        assertThat(reRegisteredFarmers.getRightThumb(), is(equalTo(reRegisteredFarmersList.get(0).getRightThumb())));

        db.clearTable(db.TABLE_RE_REGISTERED_FARMERS);
    }

    @Test
    public void addFarmDataCollected() throws Exception {
        db.clearTable(db.TABLE_FARM_DATA_COLLECTED);

        FarmDataColleted farmDataColleted = randomObjectFiller.createAndFill(FarmDataColleted.class);
        db.addFarmDataCollected(farmDataColleted);

        List<FarmDataColleted> companiesList = db.getAllFarmDataCollected();

        assertThat(farmDataColleted.getFarmID(), is(equalTo(companiesList.get(0).getFarmID())));
        assertThat(farmDataColleted.getFormTypeID(), is(equalTo(companiesList.get(0).getFormTypeID())));

        db.clearTable(db.TABLE_FARM_DATA_COLLECTED);
    }

    @Test
    public void addOfficerTraining() throws Exception {
        db.clearTable(db.TABLE_EXT_OFFICER_TRAINING);

        OfficerTraining officerTraining = randomObjectFiller.createAndFill(OfficerTraining.class);
        db.addOfficerTraining(officerTraining);

        List<OfficerTraining> companiesList = db.getAllOfficerTraining();

        //assertThat(officerTraining.getUserId(), is(equalTo(companiesList.get(0).getUserId())));
        assertThat(officerTraining.getExtTrainID(), is(equalTo(companiesList.get(0).getExtTrainID())));
        assertThat(officerTraining.getFarmerID(), is(equalTo(companiesList.get(0).getFarmerID())));
        assertThat(officerTraining.getFarmID(), is(equalTo(companiesList.get(0).getFarmID())));
        assertThat(officerTraining.getTrainCat(), is(equalTo(companiesList.get(0).getTrainCat())));
        assertThat(officerTraining.getTrainCatID(), is(equalTo(companiesList.get(0).getTrainCatID())));
        assertThat(officerTraining.getTrainDate(), is(equalTo(companiesList.get(0).getTrainDate())));
        assertThat(officerTraining.getVillageID(), is(equalTo(companiesList.get(0).getVillageID())));

        db.clearTable(db.TABLE_EXT_OFFICER_TRAINING);
    }


    @Test
    public void addCompany() throws Exception {
        db.clearTable(db.TABLE_COMPANIES);

        Companies company = randomObjectFiller.createAndFill(Companies.class);
        db.addCompany(company);

        List<Companies> companiesList = db.getAllCompanies();

        assertThat(company.getCompanyID(), is(equalTo(companiesList.get(0).getCompanyID())));
        assertThat(company.getCompanyName(), is(equalTo(companiesList.get(0).getCompanyName())));

        db.clearTable(db.TABLE_COMPANIES);
    }

    @Test
    public void addMappedFarms() throws Exception {
        db.clearTable(db.TABLE_MAPPED_FARMS);

        MappedFarm mappedFarm = randomObjectFiller.createAndFill(MappedFarm.class);
        db.addMappedFarm(mappedFarm);

        List<MappedFarm> farms = db.getAllMappedFarms();

        assertThat(mappedFarm.getFarmId(), is(equalTo(farms.get(0).getFarmId())));
        assertThat(mappedFarm.getLatitude(), is(equalTo(farms.get(0).getLatitude())));
        assertThat(mappedFarm.getLongitude(), is(equalTo(farms.get(0).getLongitude())));
        assertThat(mappedFarm.getPoints(), is(equalTo(farms.get(0).getPoints())));
        assertThat(mappedFarm.getActualFarmArea(), is(equalTo(farms.get(0).getActualFarmArea())));
        assertThat(mappedFarm.getPerimeter(), is(equalTo(farms.get(0).getPerimeter())));
        assertThat(mappedFarm.getUserId(), is(equalTo(farms.get(0).getUserId())));
        assertThat(mappedFarm.getCompanyId(), is(equalTo(farms.get(0).getCompanyId())));

        db.clearTable(db.TABLE_MAPPED_FARMS);
    }

    @Test
    public void addCottonDeductions() throws Exception {
        db.clearTable(db.TABLE_COTTON_DEDUCTIONS);

        CottonDeduction cottonDeduction = randomObjectFiller.createAndFill(CottonDeduction.class);
        db.addCottonDeduction(cottonDeduction);

        List<CottonDeduction> cottonDeductions = db.getAllCottonDeductions();

        assertThat(cottonDeduction.getFarmerId(), is(equalTo(cottonDeductions.get(0).getFarmerId())));
        assertThat(cottonDeduction.getDeductions(), is(equalTo(cottonDeductions.get(0).getDeductions())));
        assertThat(cottonDeduction.getDeliveries(), is(equalTo(cottonDeductions.get(0).getDeliveries())));
        assertThat(cottonDeduction.getSeasonId(), is(equalTo(cottonDeductions.get(0).getSeasonId())));
        assertThat(cottonDeduction.getSeasonName(), is(equalTo(cottonDeductions.get(0).getSeasonName())));

        db.clearTable(db.TABLE_COTTON_DEDUCTIONS);
    }

    @Test
    public void addRecoveredCash() throws Exception {
        db.clearTable(db.TABLE_RECOVERED_CASH);

        RecoveredCash recoveredCash = randomObjectFiller.createAndFill(RecoveredCash.class);
        db.addRecoveredCash(recoveredCash);

        List<RecoveredCash> allRecoveredCash = db.getAllRecoveredCash();

        assertThat(recoveredCash.getFarmerId(), is(equalTo(allRecoveredCash.get(0).getFarmerId())));
        assertThat(recoveredCash.getTimes(), is(equalTo(allRecoveredCash.get(0).getTimes())));
        assertThat(recoveredCash.getTotalAmount(), is(equalTo(allRecoveredCash.get(0).getTotalAmount())));
        assertThat(recoveredCash.getSeasonId(), is(equalTo(allRecoveredCash.get(0).getSeasonId())));
        assertThat(recoveredCash.getSeasonName(), is(equalTo(allRecoveredCash.get(0).getSeasonName())));

        db.clearTable(db.TABLE_RECOVERED_CASH);
    }

    @Test
    public void addProductPurchase() throws Exception {
        db.clearTable(db.TABLE_PRODUCT_PURCHASES);

        ProductPurchase productPurchase = randomObjectFiller.createAndFill(ProductPurchase.class);
        db.addProductPurchase(productPurchase);

        List<ProductPurchase> productPurchases = db.getAllProductPurchases();

        assertThat(productPurchase.getFarmerId(), is(equalTo(productPurchases.get(0).getFarmerId())));
        assertThat(productPurchase.getPrice(), is(equalTo(productPurchases.get(0).getPrice())));
        assertThat(productPurchase.getGradeId(), is(equalTo(productPurchases.get(0).getGradeId())));
        assertThat(productPurchase.getDeductions(), is(equalTo(productPurchases.get(0).getDeductions())));
        assertThat(productPurchase.getWeight(), is(equalTo(productPurchases.get(0).getWeight())));
        assertThat(productPurchase.getReceiptNumber(), is(equalTo(productPurchases.get(0).getReceiptNumber())));
        assertThat(productPurchase.getCompanyId(), is(equalTo(productPurchases.get(0).getCompanyId())));
        assertThat(productPurchase.getUserId(), is(equalTo(productPurchases.get(0).getUserId())));

        db.clearTable(db.TABLE_PRODUCT_PURCHASES);
    }

    @Test
    public void addCollectedOrder() throws Exception {
        db.clearTable(db.TABLE_COLLECTED_ORDERS);

        CollectedOrder collectedOrder = randomObjectFiller.createAndFill(CollectedOrder.class);
        db.addCollectedOrder(collectedOrder);

        List<CollectedOrder> collectedOrders = db.getAllCollectedOrders();

        assertThat(collectedOrder.getFarmerId(), is(equalTo(collectedOrders.get(0).getFarmerId())));
        assertThat(collectedOrder.getOrders(), is(equalTo(collectedOrders.get(0).getOrders())));
        assertThat(collectedOrder.getFarmerId(), is(equalTo(collectedOrders.get(0).getFarmerId())));
        assertThat(collectedOrder.getTotalAmount(), is(equalTo(collectedOrders.get(0).getTotalAmount())));
        assertThat(collectedOrder.getSeasonId(), is(equalTo(collectedOrders.get(0).getSeasonId())));
        assertThat(collectedOrder.getSeasonName(), is(equalTo(collectedOrders.get(0).getSeasonName())));

        db.clearTable(db.TABLE_COLLECTED_ORDERS);
    }


    @Test
    public void addFarmer() throws Exception {
        db.clearTable(db.TABLE_FARMERS);

        Farmers farmer = randomObjectFiller.createAndFill(Farmers.class);
        db.addFarmer(farmer);

        List<Farmers> farmersList = db.getAllFarmers();

        assertThat(farmer.getCompanyID(), is(equalTo(farmersList.get(0).getCompanyID())));
        assertThat(farmer.getFName(), is(equalTo(farmersList.get(0).getFName())));
        assertThat(farmer.getLName(), is(equalTo(farmersList.get(0).getLName())));
        assertThat(farmer.getGender(), is(equalTo(farmersList.get(0).getGender())));
        assertThat(farmer.getIDNumber(), is(equalTo(farmersList.get(0).getIDNumber())));
        assertThat(farmer.getPhoneNumber(), is(equalTo(farmersList.get(0).getPhoneNumber())));
        assertThat(farmer.getEmail(), is(equalTo(farmersList.get(0).getEmail())));
        assertThat(farmer.getVillageID(), is(equalTo(farmersList.get(0).getVillageID())));
        assertThat(farmer.getSubVillageID(), is(equalTo(farmersList.get(0).getSubVillageID())));
        assertThat(farmer.getFarmerPicPath(), is(equalTo(farmersList.get(0).getFarmerPicPath())));
        assertThat(farmer.getLeftThumb(), is(equalTo(farmersList.get(0).getLeftThumb())));
        assertThat(farmer.getRightThumb(), is(equalTo(farmersList.get(0).getRightThumb())));
        assertThat(farmer.getLatitude(), is(equalTo(farmersList.get(0).getLatitude())));
        assertThat(farmer.getLongitude(), is(equalTo(farmersList.get(0).getLongitude())));
        assertThat(farmer.getShowIntent(), is(equalTo(farmersList.get(0).getShowIntent())));
        assertThat(farmer.getEstimatedFarmArea(), is(equalTo(farmersList.get(0).getEstimatedFarmArea())));
        assertThat(farmer.getFarmVidOne(), is(equalTo(farmersList.get(0).getFarmVidOne())));
        assertThat(farmer.getOtherCropsTwo(), is(equalTo(farmersList.get(0).getOtherCropsTwo())));
        assertThat(farmer.getFarmVidTwo(), is(equalTo(farmersList.get(0).getFarmVidTwo())));
        assertThat(farmer.getEstimatedFarmAreaThree(), is(equalTo(farmersList.get(0).getEstimatedFarmAreaThree())));
        assertThat(farmer.getFarmVidThree(), is(equalTo(farmersList.get(0).getFarmVidThree())));
        assertThat(farmer.getEstimatedFarmAreaFour(), is(equalTo(farmersList.get(0).getEstimatedFarmAreaFour())));
        assertThat(farmer.getFarmVidFour(), is(equalTo(farmersList.get(0).getFarmVidFour())));
        assertThat(farmer.getOtherCropsOne(), is(equalTo(farmersList.get(0).getOtherCropsOne())));
        assertThat(farmer.getOtherCropsTwo(), is(equalTo(farmersList.get(0).getOtherCropsTwo())));
        assertThat(farmer.getOtherCropsThree(), is(equalTo(farmersList.get(0).getOtherCropsThree())));
        assertThat(farmer.getCompanyID(), is(equalTo(farmersList.get(0).getCompanyID())));
        assertThat(farmer.getUserID(), is(equalTo(farmersList.get(0).getUserID())));

        db.clearTable(db.TABLE_FARMERS);
    }


    public class RandomObjectFiller {

        private Random random = new Random();

        public <T> T createAndFill(Class<T> clazz) throws Exception {
            T instance = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = getRandomValueForField(field);

                if(clazz.getSimpleName().equals( WackFarmer.class.getSimpleName())) {
                    if (field.getName().equals("status")) {
                        field.set(instance, "invalid");
                    } else {
                        field.set(instance, value);
                    }
                }else{
                    field.set(instance, value);
                }

            }
            return instance;
        }

        public <T> List<T> fillList(Class<T> clazz) throws Exception {
            List<T> tempList = new ArrayList<>();
            int i = 0;
            do {
                tempList.add(this.createAndFill(clazz));
                i++;
            } while (i < 4);

            return tempList;
        }

        private Object getRandomValueForField(Field field) throws Exception {
            Class<?> type = field.getType();

            // Note that we must handle the different types here! This is just an
            // example, so this list is not complete! Adapt this to your needs!
            if (type.isEnum()) {
                Object[] enumValues = type.getEnumConstants();
                return enumValues[random.nextInt(enumValues.length)];
            } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
                return random.nextInt();
            } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
                return random.nextLong();
            } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
                return random.nextDouble();
            } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
                return random.nextFloat();
            } else if (type.equals(String.class)) {
                return UUID.randomUUID().toString();
            } else if (type.equals(BigInteger.class)) {
                return BigInteger.valueOf(random.nextInt());
            }
            return createAndFill(type);
        }
    }

}
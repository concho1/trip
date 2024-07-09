package com.goott.trip.concho.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.HotelSentiment;
import com.goott.trip.concho.service.module.AmadeusApiModuleService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class testConchoController {
    private final AmadeusApiModuleService amadeusApiModuleService;

    @GetMapping("test")
    public ModelAndView test() {
        try {
            Hotel[] hotels = amadeusApiModuleService.getHotelListByIataCode("LON");
            String url = "https://www.tripadvisor.com/Search?q=DOUBLETREE+LONDON+EALING&geo=1&ssrc=h&searchNearby=false";
            String filePath = "tripadvisor_reviews.txt";
            int i = 0;
            for (Hotel hotel : hotels) {
                i++;
                if (i > 2) break;
                System.out.println("==============================");
                System.out.println(hotelToString(hotel));
                try {
                    HotelOfferSearch[] hotelOfferSearches = amadeusApiModuleService.getHotelRoomById(hotel.getHotelId(), 2, "2024-08-12", "2024-08-17");
                    HotelSentiment hotelSentiment = amadeusApiModuleService.getHotelRatingByHotelId(hotel.getHotelId());

                    // Connect to the website and get the document with a custom User-Agent
                    Document document = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            .get();

                    // Get the entire HTML of the document
                    String fullDocument = document.outerHtml();

                    // Write the full document to a file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        writer.write(fullDocument);
                    }

                    System.out.println("Full document saved to " + filePath);


                    System.out.println("호텔 객실 제안 목록================");
                    for (HotelOfferSearch hotelOfferSearch : hotelOfferSearches) {
                        //System.out.println(hotelOfferSearchToString(hotelOfferSearch));
                    }
                    System.out.println("호텔 평가================");
                    System.out.println(hotelSentimentToString(hotelSentiment));
                } catch (ResponseException e) {
                    if (e.getResponse().getStatusCode() == 400) {
                        //System.out.println("Error: Minimum stay required for hotel ID: " + hotel.getHotelId());
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String hotelToString(Hotel hotel) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hotel Details: \n");
        sb.append("Subtype: ").append(hotel.getSubtype()).append("\n");
        sb.append("Name: ").append(hotel.getName()).append("\n");
        sb.append("Time Zone Name: ").append(hotel.getTimeZoneName()).append("\n");
        sb.append("IATA Code: ").append(hotel.getIataCode()).append("\n");
        sb.append("Address: ").append(addressToString(hotel.getAddress())).append("\n");
        sb.append("GeoCode: ").append(geoCodeToString(hotel.getGeoCode())).append("\n");
        sb.append("Google Place ID: ").append(hotel.getGooglePlaceId()).append("\n");
        sb.append("OpenJet Airport ID: ").append(hotel.getOpenjetAirportId()).append("\n");
        sb.append("UIC Code: ").append(hotel.getUicCode()).append("\n");
        sb.append("Hotel ID: ").append(hotel.getHotelId()).append("\n");
        sb.append("Chain Code: ").append(hotel.getChainCode()).append("\n");
        sb.append("Distance: ").append(distanceToString(hotel.getDistance())).append("\n");
        sb.append("Last Update: ").append(hotel.getLastUpdate()).append("\n");
        return sb.toString();
    }

    private String addressToString(Hotel.Address address) {
        if (address == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("Category: ").append(address.getCategory()).append("\n");
        sb.append("Lines: ").append(Arrays.toString(address.getLines())).append("\n");
        sb.append("Postal Code: ").append(address.getPostalCode()).append("\n");
        sb.append("Country Code: ").append(address.getCountryCode()).append("\n");
        sb.append("City Name: ").append(address.getCityName()).append("\n");
        sb.append("State Code: ").append(address.getStateCode()).append("\n");
        sb.append("Postal Box: ").append(address.getPostalBox()).append("\n");
        sb.append("Text: ").append(address.getText()).append("\n");
        sb.append("State: ").append(address.getState()).append("\n");
        return sb.toString();
    }

    private String geoCodeToString(Hotel.GeoCode geoCode) {
        if (geoCode == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("Latitude: ").append(geoCode.getLatitude()).append("\n");
        sb.append("Longitude: ").append(geoCode.getLongitude()).append("\n");
        return sb.toString();
    }

    private String distanceToString(Hotel.Distance distance) {
        if (distance == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("Unit: ").append(distance.getUnit()).append("\n");
        sb.append("Value: ").append(distance.getValue()).append("\n");
        sb.append("Display Value: ").append(distance.getDisplayValue()).append("\n");
        sb.append("Is Unlimited: ").append(distance.getIsUnlimited()).append("\n");
        return sb.toString();
    }

    private String hotelOfferSearchToString(HotelOfferSearch hotelOfferSearch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hotel Offer Search Details: \n");
        sb.append("Type: ").append(hotelOfferSearch.getType()).append("\n");
        sb.append("Hotel: \n").append(hotelOfferSearchHotelToString(hotelOfferSearch.getHotel())).append("\n");
        sb.append("Available: ").append(hotelOfferSearch.isAvailable()).append("\n");
        sb.append("Offers: \n").append(offersToString(hotelOfferSearch.getOffers())).append("\n");
        sb.append("Self: ").append(hotelOfferSearch.getSelf()).append("\n");
        return sb.toString();
    }

    private String hotelOfferSearchHotelToString(HotelOfferSearch.Hotel hotel) {
        if (hotel == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("  Type: ").append(hotel.getType()).append("\n");
        sb.append("  Hotel ID: ").append(hotel.getHotelId()).append("\n");
        sb.append("  Chain Code: ").append(hotel.getChainCode()).append("\n");
        sb.append("  Brand Code: ").append(hotel.getBrandCode()).append("\n");
        sb.append("  Dupe ID: ").append(hotel.getDupeId()).append("\n");
        sb.append("  Name: ").append(hotel.getName()).append("\n");
        sb.append("  City Code: ").append(hotel.getCityCode()).append("\n");
        sb.append("  Latitude: ").append(hotel.getLatitude()).append("\n");
        sb.append("  Longitude: ").append(hotel.getLongitude()).append("\n");
        return sb.toString();
    }

    private String offersToString(HotelOfferSearch.Offer[] offers) {
        if (offers == null || offers.length == 0) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (HotelOfferSearch.Offer offer : offers) {
            sb.append("  Offer ID: ").append(offer.getId()).append("\n");
            sb.append("  Check-in Date: ").append(offer.getCheckInDate()).append("\n");
            sb.append("  Check-out Date: ").append(offer.getCheckOutDate()).append("\n");
            sb.append("  Room Type: ").append(offer.getRoom().getType()).append("\n");
            sb.append("  Room Details: \n").append(roomDetailsToString(offer.getRoom())).append("\n");
            sb.append("  Guests: \n").append(guestsToString(offer.getGuests())).append("\n");
            sb.append("  Price: \n").append(hotelPriceToString(offer.getPrice())).append("\n");
            sb.append("  Policies: \n").append(policiesToString(offer.getPolicies())).append("\n");
            sb.append("  Self: ").append(offer.getSelf()).append("\n");
        }
        return sb.toString();
    }

    private String roomDetailsToString(HotelOfferSearch.RoomDetails room) {
        if (room == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("    Type: ").append(room.getType()).append("\n");
        sb.append("    Estimated Room Type: \n").append(estimatedRoomTypeToString(room.getTypeEstimated())).append("\n");
        sb.append("    Description: \n").append(qualifiedFreeTextToString(room.getDescription())).append("\n");
        return sb.toString();
    }

    private String estimatedRoomTypeToString(HotelOfferSearch.EstimatedRoomType roomType) {
        if (roomType == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Category: ").append(roomType.getCategory()).append("\n");
        sb.append("      Beds: ").append(roomType.getBeds()).append("\n");
        sb.append("      Bed Type: ").append(roomType.getBedType()).append("\n");
        return sb.toString();
    }

    private String qualifiedFreeTextToString(HotelOfferSearch.QualifiedFreeText text) {
        if (text == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Language: ").append(text.getLang()).append("\n");
        sb.append("      Text: ").append(text.getText()).append("\n");
        return sb.toString();
    }

    private String guestsToString(HotelOfferSearch.Guests guests) {
        if (guests == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("    Adults: ").append(guests.getAdults()).append("\n");
        sb.append("    Child Ages: ").append(Arrays.toString(guests.getChildAges())).append("\n");
        return sb.toString();
    }

    private String hotelPriceToString(HotelOfferSearch.HotelPrice price) {
        if (price == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("    Currency: ").append(price.getCurrency()).append("\n");
        sb.append("    Selling Total: ").append(price.getSellingTotal()).append("\n");
        sb.append("    Total: ").append(price.getTotal()).append("\n");
        sb.append("    Base: ").append(price.getBase()).append("\n");
        sb.append("    Taxes: \n").append(taxesToString(price.getTaxes())).append("\n");
        sb.append("    Markups: ").append(Arrays.toString(price.getMarkups())).append("\n");
        sb.append("    Variations: \n").append(priceVariationsToString(price.getVariations())).append("\n");
        return sb.toString();
    }

    private String taxesToString(HotelOfferSearch.HotelTax[] taxes) {
        if (taxes == null || taxes.length == 0) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (HotelOfferSearch.HotelTax tax : taxes) {
            sb.append("      Currency: ").append(tax.getCurrency()).append("\n");
            sb.append("      Amount: ").append(tax.getAmount()).append("\n");
            sb.append("      Code: ").append(tax.getCode()).append("\n");
            sb.append("      Percentage: ").append(tax.getPercentage()).append("\n");
            sb.append("      Included: ").append(tax.isIncluded()).append("\n");
            sb.append("      Description: ").append(tax.getDescription()).append("\n");
            sb.append("      Pricing Frequency: ").append(tax.getPricingFrequency()).append("\n");
            sb.append("      Pricing Mode: ").append(tax.getPricingMode()).append("\n");
        }
        return sb.toString();
    }

    private String priceVariationsToString(HotelOfferSearch.PriceVariations variations) {
        if (variations == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Average: \n").append(priceToString(variations.getAverage())).append("\n");
        sb.append("      Changes: \n").append(priceVariationsArrayToString(variations.getChanges())).append("\n");
        return sb.toString();
    }

    private String priceToString(HotelOfferSearch.Price price) {
        if (price == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("        Currency: ").append(price.getCurrency()).append("\n");
        sb.append("        Selling Total: ").append(price.getSellingTotal()).append("\n");
        sb.append("        Total: ").append(price.getTotal()).append("\n");
        sb.append("        Base: ").append(price.getBase()).append("\n");
        sb.append("        Markups: ").append(Arrays.toString(price.getMarkups())).append("\n");
        return sb.toString();
    }

    private String priceVariationsArrayToString(HotelOfferSearch.PriceVariation[] variations) {
        if (variations == null || variations.length == 0) return "N/A";
        StringBuilder sb = new StringBuilder();
        for (HotelOfferSearch.PriceVariation variation : variations) {
            sb.append("        Start Date: ").append(variation.getStartDate()).append("\n");
            sb.append("        End Date: ").append(variation.getEndDate()).append("\n");
            sb.append("        Currency: ").append(variation.getCurrency()).append("\n");
            sb.append("        Selling Total: ").append(variation.getSellingTotal()).append("\n");
            sb.append("        Base: ").append(variation.getBase()).append("\n");
            sb.append("        Total: ").append(variation.getTotal()).append("\n");
            sb.append("        Markups: ").append(Arrays.toString(variation.getMarkups())).append("\n");
        }
        return sb.toString();
    }

    private String policiesToString(HotelOfferSearch.PolicyDetails policies) {
        if (policies == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("    Payment Type: ").append(policies.getPaymentType()).append("\n");
        sb.append("    Guarantee: \n").append(guaranteePolicyToString(policies.getGuarantee())).append("\n");
        sb.append("    Deposit: \n").append(depositPolicyToString(policies.getDeposit())).append("\n");
        sb.append("    Prepay: \n").append(depositPolicyToString(policies.getPrepay())).append("\n");
        sb.append("    Hold Time: \n").append(holdPolicyToString(policies.getHoldTime())).append("\n");
        sb.append("    Cancellation: \n").append(cancellationPolicyToString(policies.getCancellation())).append("\n");
        sb.append("    Check-In/Out: \n").append(checkInOutPolicyToString(policies.getCheckInOut())).append("\n");
        return sb.toString();
    }

    private String guaranteePolicyToString(HotelOfferSearch.GuaranteePolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Description: \n").append(qualifiedFreeTextToString(policy.getDescription())).append("\n");
        sb.append("      Accepted Payments: \n").append(paymentPolicyToString(policy.getAcceptedPayments())).append("\n");
        return sb.toString();
    }

    private String depositPolicyToString(HotelOfferSearch.DepositPolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Amount: ").append(policy.getAmount()).append("\n");
        sb.append("      Deadline: ").append(policy.getDeadline()).append("\n");
        sb.append("      Description: \n").append(qualifiedFreeTextToString(policy.getDescription())).append("\n");
        sb.append("      Accepted Payments: \n").append(paymentPolicyToString(policy.getAcceptedPayments())).append("\n");
        return sb.toString();
    }

    private String holdPolicyToString(HotelOfferSearch.HoldPolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Deadline: ").append(policy.getDeadline()).append("\n");
        return sb.toString();
    }

    private String cancellationPolicyToString(HotelOfferSearch.CancellationPolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Type: ").append(policy.getType()).append("\n");
        sb.append("      Amount: ").append(policy.getAmount()).append("\n");
        sb.append("      Number of Nights: ").append(policy.getNumberOfNights()).append("\n");
        sb.append("      Percentage: ").append(policy.getPercentage()).append("\n");
        sb.append("      Deadline: ").append(policy.getDeadline()).append("\n");
        sb.append("      Description: \n").append(qualifiedFreeTextToString(policy.getDescription())).append("\n");
        return sb.toString();
    }

    private String checkInOutPolicyToString(HotelOfferSearch.CheckInOutPolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Check-In: ").append(policy.getCheckIn()).append("\n");
        sb.append("      Check-In Description: \n").append(qualifiedFreeTextToString(policy.getCheckInDescription())).append("\n");
        sb.append("      Check-Out: ").append(policy.getCheckOut()).append("\n");
        sb.append("      Check-Out Description: \n").append(qualifiedFreeTextToString(policy.getCheckOutDescription())).append("\n");
        return sb.toString();
    }

    private String paymentPolicyToString(HotelOfferSearch.PaymentPolicy policy) {
        if (policy == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("      Credit Cards: ").append(Arrays.toString(policy.getCreditCards())).append("\n");
        sb.append("      Method: ").append(Arrays.toString(policy.getMethod())).append("\n");
        return sb.toString();
    }

    private String hotelSentimentToString(HotelSentiment hotelSentiment) {
        if (hotelSentiment == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("Hotel Sentiment: \n");
        sb.append("Hotel ID: ").append(hotelSentiment.getHotelId()).append("\n");
        sb.append("Type: ").append(hotelSentiment.getType()).append("\n");
        sb.append("Overall Rating: ").append(hotelSentiment.getOverallRating()).append("\n");
        sb.append("Number of Reviews: ").append(hotelSentiment.getNumberOfReviews()).append("\n");
        sb.append("Sentiments: \n").append(sentimentToString(hotelSentiment.getSentiments())).append("\n");
        return sb.toString();
    }

    private String sentimentToString(HotelSentiment.Sentiment sentiment) {
        if (sentiment == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("  Staff: ").append(sentiment.getStaff()).append("\n");
        sb.append("  Location: ").append(sentiment.getLocation()).append("\n");
        sb.append("  Service: ").append(sentiment.getService()).append("\n");
        sb.append("  Room Comforts: ").append(sentiment.getRoomComforts()).append("\n");
        sb.append("  Sleep Quality: ").append(sentiment.getSleepQuality()).append("\n");
        sb.append("  Swimming Pool: ").append(sentiment.getSwimmingPool()).append("\n");
        sb.append("  Value for Money: ").append(sentiment.getValueForMoney()).append("\n");
        sb.append("  Facilities: ").append(sentiment.getFacilities()).append("\n");
        sb.append("  Catering: ").append(sentiment.getCatering()).append("\n");
        sb.append("  Points of Interest: ").append(sentiment.getPointsOfInterest()).append("\n");
        return sb.toString();
    }
}

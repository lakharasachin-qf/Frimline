package com.app.frimline.models.CategoryRootFragments;

import java.util.ArrayList;

public class ReviewRootModel {

    private ArrayList<Review> reviewsList = new ArrayList<>();

    public ArrayList<Review> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(ArrayList<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public class Review {
        private String id;
        private String status;
        private String reviewerEmail;
        private String rating;
        private String review;
        private String userAvatar;

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReviewerEmail() {
            return reviewerEmail;
        }

        public void setReviewerEmail(String reviewerEmail) {
            this.reviewerEmail = reviewerEmail;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
//           /* "id": 1617,
//        "date_created": "2021-08-05T23:22:11",
//        "date_created_gmt": "2021-08-05T17:52:11",
//        "product_id": 9659,
//        "status": "approved",
//        "reviewer": "admin",
//        "reviewer_email": "info@frimline.store",
//        "review": "<p>ffvfvfvfvfv</p>\n",
//        "rating": 5,
//        "verified": false,
//        "reviewer_avatar_urls": {
//            "24": "https://secure.gravatar.com/avatar/727b30068d4280ddee7fcbd6d14ab2fb?s=24&d=mm&r=g",
//            "48": "https://secure.gravatar.com/avatar/727b30068d4280ddee7fcbd6d14ab2fb?s=48&d=mm&r=g",
//            "96": "https://secure.gravatar.com/avatar/727b30068d4280ddee7fcbd6d14ab2fb?s=96&d=mm&r=g"
//        },
//        "_links": {
//            "self": [
//                {
//                    "href": "https://frimline.queryfinders.com/wp-json/wc/v3/products/reviews/1617"
//                }
//            ],
//            "collection": [
//                {
//                    "href": "https://frimline.queryfinders.com/wp-json/wc/v3/products/reviews"
//                }
//            ],
//            "up": [
//                {
//                    "href": "https://frimline.queryfinders.com/wp-json/wc/v3/products/9659"
//                }
//            ],
//            "reviewer": [
//                {
//                    "embeddable": true,
//                    "href": "https://frimline.queryfinders.com/wp-json/wp/v2/users/1"
//                }
//            ]
//        }*/

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }
}

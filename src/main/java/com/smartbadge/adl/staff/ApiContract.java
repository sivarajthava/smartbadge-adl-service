package com.smartbadge.adl.staff;

public class ApiContract {

	public static final String GET_STAFF_JSON = """
			{
			  "staffId": "E0001",
			  "name": "Rohan Kumar",
			  "email": "Rohan@gmail.com",
			  "title": "MR",
			  "jobTitle": "Senior Software Engineer",
			  "grade": "IT.07",
			  "businessCardType": "SMARTBADGE",
			  "addresses": [
			    {
			      "address": "RESIDENCE",
			      "street": "1st street",
			      "city": "Pisanathur",
			      "country": "India",
			      "addressType": "RESIDENCE"
			    },
			    {
			      "address": "CURRENT",
			      "street": "Doha Street",
			      "city": "Dubai",
			      "country": "UAE",
			      "addressType": "CURRENT"
			    }
			  ],
			  "leaveDocumentDto": {
			    "leaveBalance": 3,
			    "staffId": "E486193",
			    "leaves": [
			      {
			        "referenceId": "LEV-REQ-1778668787173",
			        "leaveType": "ANNUAL_LEAVE",
			        "leaveStatus": "APPROVED",
			        "fromDate": "23/05/2026",
			        "toDate": "24/05/2026",
			        "reason": "EID Holiday"
			      },
			      {
			        "referenceId": "LEV-REQ-1778619360977",
			        "leaveType": "SICK_LEAVE",
			        "leaveStatus": "APPROVED",
			        "fromDate": "14/05/2026",
			        "toDate": "14/05/2026",
			        "reason": "Login is torturing",
			        "comments": "Enjoy your meals"
			      }
			    ]
			  },
			  "teamMembers": {
			    "staffId": "S4861934",
			    "teamMembers": [
			      {
			        "staffId": "E486223",
			        "name": "Sigam Logan",
			        "status": "ACTIVE",
			        "role": "Lead"
			      },
			      {
			        "staffId": "E5789",
			        "name": "Sivaraj Thavamani",
			        "status": "INACTIVE",
			        "role": "Labour"
			      },
			      {
			        "staffId": "E22222",
			        "name": "Wajeaha",
			        "status": "ACTIVE",
			        "role": "Data Engineer"
			      }
			    ]
			  }
			}
			""";

	public static final String SAVE_STAFF_JSON = """
			  {
			  "success": true,
			  "data": {
			    "staffId": "E0001",
			    "name": "Rohan Kumar",
			    "email": "Rohan@gmail.com",
			    "title": "DR",
			    "jobTitle": "Senior Software Engineer",
			    "grade": "IT.07",
			    "businessCardType": "SMARTBADGE",
			    "leaves": {
			      "leaveBalance": 31,
			      "staffId": "E0001",
			      "leaves": [
			        {
			          "referenceId": "LEV-REQ-1778906906954",
			          "leaveType": "ANNUAL_LEAVE",
			          "leaveStatus": "APPROVED",
			          "fromDate": "23/05/2026",
			          "toDate": "24/05/2026",
			          "reason": "EID Holiday",
			          "comments": "EID Holiday"
			        },
			        {
			          "referenceId": "LEV-REQ-1778907183009",
			          "leaveType": "SICK_LEAVE",
			          "leaveStatus": "PENDING",
			          "fromDate": "23/05/2026",
			          "toDate": "24/05/2026",
			          "reason": "NOT FEELING WELL - Fever"
			        }
			      ]
			    },
			    "teamMembers": {
			      "staffId": "E0001",
			      "teamMembers": [
			        {
			          "staffId": "E486223",
			          "name": "Sigam Logan",
			          "status": "ACTIVE",
			          "role": "Lead"
			        },
			        {
			          "staffId": "E5789",
			          "name": "Sivaraj Thavamani",
			          "status": "INACTIVE",
			          "role": "Labour"
			        },
			        {
			          "staffId": "E22222",
			          "name": "Anila",
			          "status": "ACTIVE",
			          "role": "Data Engineer"
			        }
			      ]
			    },
			    "addresses": [
			      {
			        "address": "RESIDENCE",
			        "street": "1st street",
			        "city": "Pisanathur",
			        "country": "India",
			        "addressType": "RESIDENCE"
			      },
			      {
			        "address": "CURRENT",
			        "street": "Doha Street",
			        "city": "Dubai",
			        "country": "UAE",
			        "addressType": "CURRENT"
			      }
			    ]
			  },
			  "timestamp": "2026-05-16T06:44:23.462Z"
			}
			""";
}

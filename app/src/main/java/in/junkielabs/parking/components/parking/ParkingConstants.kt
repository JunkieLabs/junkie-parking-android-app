package `in`.junkielabs.parking.components.parking

/**
 * Created by Niraj on 28-10-2021.
 */
class ParkingConstants {

    object Wheeler {

        var TYPE_BIKE = 0x1
        var TYPE_CAR = 0x2

        fun getWheelerType(type: Int): String {
            if (type == TYPE_BIKE) return "2 Wheeler";
            else if (type == TYPE_CAR) return "4 Wheeler";
            else return ""
        }

    }

    object Vehicle {
        var INPUT_FORMAT = "XX XX XX XXXX"
//        var INPUT_FORMAT = "XX XX XX XXXX"
    }

    object CheckInOut {
        var STATUS_ACTIVE = 0x0
        var STATUS_COMPLETED = 0x1
    }
}
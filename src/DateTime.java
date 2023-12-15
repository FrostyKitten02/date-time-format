import java.io.Serializable;
import java.util.Arrays;

//storing day millis in 4-bytes (needs 3-bytes and 3-bits from 0-86.400.000)
//storing months and days in 2-bytes (needs 2-byte and 5-bits from 0-12 for months and 0-31 for days)

//TODO implement custom serialization for this class to minimize storage space used
public class DateTime implements Serializable {
    private byte[] dateContent = new byte[5];//temp mostly for debuging
    private int millisInDay;//0-86.400.000
    private int dayInMonth;//0-31
    private int month;//0-11
    private int year;//0-8191

    private static final int BYTE_OFFSET = 128;
    private DateTime(int millisInDay, int dayInMonth, int month, int year) {
        this.millisInDay = millisInDay;
        this.dayInMonth = dayInMonth;
        this.month = month;
        this.year = year;
    }

    //alternative: use millis to store millis since first day of month, and then store month and year seperate!?? mybe better

    //BYTE STRUCTURE:
    //first 27 bits are millis in a day (0-86.400.000) - a bit of space left at last byte could make this more precise if we need to
    //next 5 bits are days (0-31)
    //next 4 bits are months (0-12)
    //next 13 bits are years (0-8192) --could use space left for time zones or and for more precise day millis, or even longer years!
    //total of 49 bits used (6 bytes and 1 bit)
    public static DateTime fromByteArray(byte[] rawBytes) {
        if (rawBytes.length != 7) {
            throw new RuntimeException("Invalid byte array length for DateTime");
        }

        //TODO check if values are valid are not negative and dont exceed max values
        int dayMillis = getDayMillis(rawBytes);

        int dayInMonth = ((rawBytes[3] + BYTE_OFFSET) << 3) >> 3;
        int month = (rawBytes[4] + BYTE_OFFSET) >> 4;
        int year = getYear(rawBytes);

        return new DateTime(dayMillis, dayInMonth, month, year);
    }

    private static int getYear(byte[] rawBytes) {
        int yearChunk1 = (rawBytes[5] + BYTE_OFFSET) << 4;
        int yearChunk2 = (rawBytes[6] + BYTE_OFFSET) << 8;

        return yearChunk1 + yearChunk2;
    }

    private static int getDayMillis(byte[] rawBytes) {
        int dayChunk1 = (rawBytes[0] + BYTE_OFFSET) << 19;
        int dayChunk2 = (rawBytes[1] + BYTE_OFFSET) << 11;
        int dayChunk3 = (rawBytes[2] + BYTE_OFFSET) << 3;
        int dayChunk4 = (rawBytes[3] + BYTE_OFFSET) >> 5;

        return dayChunk1 + dayChunk2 + dayChunk3 + dayChunk4;
    }

    private static void testDateTimeFromByte() {
        byte[] b2 = new byte[] {36, 75, 0, -122, -48, -128, -128};
        testDateTimeFromByteArraySingle(b2, 86_400_000, 6, 5, 0);
    }

    private static void testDateTimeFromByteArraySingle(byte[] b1, int millisInDay, int dayInMonth, int month, int year) {
        DateTime d = DateTime.fromByteArray(b1);
        if (d.millisInDay != millisInDay) {
            System.err.println("Expected millisInDay: " + millisInDay + " but got: " + d.millisInDay);
        }
        if (d.dayInMonth != dayInMonth) {
            System.err.println("Expected dayInMonth: " + dayInMonth + " but got: " + d.dayInMonth);
        }
        if (d.month != month) {
            System.err.println("Expected month: " + month + " but got: " + d.month);
        }
        if (d.year != year) {
            System.err.println("Expected year: " + year + " but got: " + d.year);
        }
    }

    //TEMPORARAY TEST LOCATION
    private static void testDateTimeFromByteArrayMillis() {
        byte[] b1 = new byte[] {127, 127, 127, 127, 127, 127, 127};
        byte[] b2 = new byte[] {36, 75, 0, -128, 127, 127, 127};
        testSingleTimeFromByteArrayMillis(134_217_727, b1);
        testSingleTimeFromByteArrayMillis(86_400_000, b2);
        System.out.println("Finished testing DateTime from byte array millis");
    }

    private static boolean testSingleTimeFromByteArrayMillis(long expected, byte[] b1) {
        long got = DateTime.getDayMillis(b1);

        if (expected != got) {
            System.err.println("Expected: " + expected + " but got: " + got);
            return false;
        }

        return true;
    }



    public static void main(String[] args) {
//        DateTime.testDateTimeFromByteArrayMillis();
        testDateTimeFromByte();

        //DateTime d = DateTime.fromByteArray(new byte[] {-128, -128, -128, -128, -128, -128, -128});
//        DateTime d2 = DateTime.fromByteArray();

//        byte b1 = 0b1111111;
//        byte b2 = 0b1111110;
//        byte b3 = 0b0111111;
//        byte b4 = 0b1100011;
//
//        long l1 = (long)(b1 + 128) << 0;
//        long l2 = (long)(b1 + 128) << 8;
//        long l3 = (long)(b1 + 128) << 16;
//        long l4 = (long)(b1 + 128) << 24;
//
//        String st1 = Long.toBinaryString(l1);
//        String st2 = Long.toBinaryString(l2);
//        String st3 = Long.toBinaryString(l3);
//        String st4 = Long.toBinaryString(l4);

        System.out.println("wow");
    }
}
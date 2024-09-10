// package rs.ac.uns.ftn.BookingBaboon.config;
// public class PasswordHelper {
//     // it must be at least 8 chars long and no more than 64
//     // it must contain characters from three of the following four categories:
//     //    English uppercase characters (A through Z)
//     //    English lowercase characters (a through z)
//     //    Base 10 digits (0 through 9)
//     //    Non-alphabetic characters (for example, !, $, #, %)
//     public static Boolean isValid(String password) {
//         int upperCaseCount = 0;
//         int lowerCaseCount = 0;
//         int digitCount = 0;
//         int nonAlphaCount = 0;
//         for (char ch: password.toCharArray()) {
//             if (ch >= 'a' && ch <= 'z') ++lowerCaseCount;
//             else if (ch >= 'A' && ch <= 'Z') ++upperCaseCount;
//             else if (ch >= '0' && ch <= '9') ++digitCount;
//             else ++nonAlphaCount;
//         }

//         int categoryCount = 0;
//         if (upperCaseCount > 0) ++categoryCount;
//         if (lowerCaseCount > 0) ++categoryCount;
//         if (digitCount > 0) ++categoryCount;
//         if (nonAlphaCount > 0) ++categoryCount;

//         return (categoryCount >= 3) && (password.length() >= 8) && (password.length() <= 64);
//     }
// }
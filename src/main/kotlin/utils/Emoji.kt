package utils

/*
 * AIBot by AlienIdeology
 *
 * Emoji
 * Static fields for global uses of Emoji
 */


import com.vdurmont.emoji.EmojiParser

/**
 *
 * @author liaoyilin
 */
class Emoji {

    object Unicode {
        val zero_width = "\u200B"
    }

    companion object {

        /* General */
        val SUCCESS = EmojiParser.parseToUnicode(":white_check_mark:")
        val ERROR = EmojiParser.parseToUnicode(":fire:")
        val YES = EmojiParser.parseToUnicode(":o:")
        val NO = EmojiParser.parseToUnicode(":x:")
        val IN = EmojiParser.parseToUnicode(":inbox_tray:")
        val OUT = EmojiParser.parseToUnicode(":outbox_tray:")
        val UP = EmojiParser.parseToUnicode(":arrow_up:")
        val DOWN = EmojiParser.parseToUnicode(":arrow_down:")
        val RIGHT = EmojiParser.parseToUnicode(":arrow_right:")
        val LEFT = EmojiParser.parseToUnicode(":arrow_left:")
        val INVITE = EmojiParser.parseToUnicode(":postbox:")
        val INFORMATION = EmojiParser.parseToUnicode(":information_source:")
        val BAN = EmojiParser.parseToUnicode(":hammer:")

        /* Faces */
        val FACE_TONGUE = EmojiParser.parseToUnicode(":stuck_out_tongue:")
        val FACE_BLUSH = EmojiParser.parseToUnicode(":blush:")

        /* Hand */
        val HAND_OK = EmojiParser.parseToUnicode(":ok_hand:")
        val HAND_WAVE = EmojiParser.parseToUnicode(":wave:")
        val HAND_RAISE = EmojiParser.parseToUnicode(":raised_hand:")
        val HAND_HORN = EmojiParser.parseToUnicode(":horns_sign:")
        val HAND_MUSCLE = EmojiParser.parseToUnicode(":muscle:")
        val THUMB_UP = EmojiParser.parseToUnicode(":thumbsup:")
        val THUMB_DOWN = EmojiParser.parseToUnicode(":thumbsdown:")
        val POINT_UP = EmojiParser.parseToUnicode(":point_up:")
        val POINT_DOWN = EmojiParser.parseToUnicode(":point_down:")
        val POINT_LEFT = EmojiParser.parseToUnicode(":point_left:")
        val POINT_RIGHT = EmojiParser.parseToUnicode(":point_right:")
        val PRAY = EmojiParser.parseToUnicode(":pray:")

        /* Numbers */
        val ONE = EmojiParser.parseToUnicode(":one:")
        val TWO = EmojiParser.parseToUnicode(":two:")
        val THREE = EmojiParser.parseToUnicode(":three:")
        val FOUR = EmojiParser.parseToUnicode(":four:")
        val FIVE = EmojiParser.parseToUnicode(":five:")
        val SIX = EmojiParser.parseToUnicode(":six:")
        val SEVEN = EmojiParser.parseToUnicode(":seven:")
        val EIGHT = EmojiParser.parseToUnicode(":eight:")
        val NINE = EmojiParser.parseToUnicode(":nine:")
        val ZERO = EmojiParser.parseToUnicode(":zero:")
        val HUNDRED = EmojiParser.parseToUnicode(":100:")

        /* Information Commands */
        val ENVELOPE = EmojiParser.parseToUnicode(":incoming_envelope:")
        val PING = EmojiParser.parseToUnicode(":ping_pong:")
        val HEART_BEAT = EmojiParser.parseToUnicode(":heartbeat:")
        val STOPWATCH = EmojiParser.parseToUnicode(":stopwatch:")
        val STATUS = EmojiParser.parseToUnicode(":vertical_traffic_light:")
        val STATISTIC = EmojiParser.parseToUnicode(":bar_chart:")
        val GUILDS = EmojiParser.parseToUnicode(":card_file_box:")
        val SHARDS = EmojiParser.parseToUnicode(":file_cabinet:")
        val TEXT = EmojiParser.parseToUnicode(":speech_balloon:")
        val SPY = EmojiParser.parseToUnicode(":spy:")

        /* Utility Commands */
        /* NumberCommand */
        val NUMBER = EmojiParser.parseToUnicode(":1234:")
        val PRINT = EmojiParser.parseToUnicode(":printer:")
        val ROLL = EmojiParser.parseToUnicode(":game_die:")

        /* WeatherCommand */
        val TEMP = EmojiParser.parseToUnicode(":thermometer:")
        val WIND = EmojiParser.parseToUnicode(":dash:")
        /* Condition */
        val SUNNY = EmojiParser.parseToUnicode(":sunny:")
        val CLOUD = EmojiParser.parseToUnicode(":cloud:")
        val CLOUD_PART = EmojiParser.parseToUnicode(":white_sun_small_cloud:")
        val CLOUDY = EmojiParser.parseToUnicode(":white_sun_behind_cloud:")
        val CLOUDY_RAIN = EmojiParser.parseToUnicode(":white_sun_behind_cloud_rain:")
        val CLOUD_RAIN = EmojiParser.parseToUnicode(":cloud_rain:")
        val CLOUD_THUNDER_RAIN = EmojiParser.parseToUnicode(":thunder_cloud_rain:")
        val CLOUD_TORNADO = EmojiParser.parseToUnicode(":cloud_tornado:")
        val SNOW = EmojiParser.parseToUnicode(":cloud_snow:")
        val WINDY = EmojiParser.parseToUnicode(":blowing_wind:")
        val SNOWMAN = EmojiParser.parseToUnicode(":snowing_snowman:")
        val SWEAT = EmojiParser.parseToUnicode(":sweat:")
        val PRESS = EmojiParser.parseToUnicode(":compression:")
        val EYES = EmojiParser.parseToUnicode(":eyes:")
        /* EmojiCommmand */
        val ABC = EmojiParser.parseToUnicode(":abc:")
        val ABCD = EmojiParser.parseToUnicode(":abcd:")
        val VS = EmojiParser.parseToUnicode(":vs:")
        val COOL = EmojiParser.parseToUnicode(":cool:")
        val OK = EmojiParser.parseToUnicode(":ok:")
        val SYMBOLS = EmojiParser.parseToUnicode(":symbols:")
        val NEW_WORD = EmojiParser.parseToUnicode(":new:")
        val FREE = EmojiParser.parseToUnicode(":free:")
        val MARK_QUESTION = EmojiParser.parseToUnicode(":grey_question:")
        val MARK_EXCLAMATION = EmojiParser.parseToUnicode(":exclamation:")
        val MARK_HASH = EmojiParser.parseToUnicode(":hash:")
        val MARK_ASTERISK = EmojiParser.parseToUnicode(":keycap_asterisk:")
        val MARK_DOLLAR_SIGN = EmojiParser.parseToUnicode(":heavy_dollar_sign:")
        val MARK_PLUS_SIGN = EmojiParser.parseToUnicode(":heavy_plus_sign:")
        val MARK_MINUS_SIGN = EmojiParser.parseToUnicode(":heavy_minus_sign:")
        val MARK_DEVIDE_SIGN = EmojiParser.parseToUnicode(":heavy_division_sign:")
        val DOT = EmojiParser.parseToUnicode(":black_circle_for_record:")

        /* SearchCommand */
        var SEARCH = EmojiParser.parseToUnicode(":mag:")
        /* IMDbCommand */
        val FILM_PROJECTOR = EmojiParser.parseToUnicode(":film_projector:")
        val FILM_FRAMES = EmojiParser.parseToUnicode(":film_frames:")
        val DATE = EmojiParser.parseToUnicode(":date:")
        val STAR = EmojiParser.parseToUnicode(":star:")
        val STARS = EmojiParser.parseToUnicode(":stars:")
        val TROPHY = EmojiParser.parseToUnicode(":trophy:")
        val BOOK = EmojiParser.parseToUnicode(":book:")

        /* Music */
        val MUSIC = EmojiParser.parseToUnicode(":musical_keyboard:")
        val PLAYER = EmojiParser.parseToUnicode(":black_right_pointing_triangle_with_double_vertical_bar:")
        val GLOBE = EmojiParser.parseToUnicode(":globe_with_meridians:")
        val NOTES = EmojiParser.parseToUnicode(":notes:")
        val FM = EmojiParser.parseToUnicode(":arrow_heading_down:")
        val RADIO = EmojiParser.parseToUnicode(":radio_button:")
        val AUTOPLAY = EmojiParser.parseToUnicode(":leftwards_arrow_with_hook:")
        val REPEAT = EmojiParser.parseToUnicode(":repeat:")
        val REPEAT_SINGLE = EmojiParser.parseToUnicode(":repeat_one:")
        val PAUSE = EmojiParser.parseToUnicode(":double_vertical_bar:")
        val NEXT_TRACK = EmojiParser.parseToUnicode(":black_right_pointing_double_triangle_with_vertical_bar:")
        val UP_VOTE = EmojiParser.parseToUnicode(":arrow_up_small:")
        val RESUME = EmojiParser.parseToUnicode(":arrow_forward:")
        val JUMP = EmojiParser.parseToUnicode(":left_right_arrow:")
        val VOLUME_NONE = EmojiParser.parseToUnicode(":mute:")
        val VOLUME_LOW = EmojiParser.parseToUnicode(":sound:")
        val VOLUME_HIGH = EmojiParser.parseToUnicode(":loud_sound:")
        val SHUFFLE = EmojiParser.parseToUnicode(":twisted_rightwards_arrows:")
        val STOP = EmojiParser.parseToUnicode(":black_square_for_stop:")

        /* Medals */
        val FIRST_PLACE = EmojiParser.parseToUnicode(":first_place:")
        val SECOND_PLACE = EmojiParser.parseToUnicode(":second_place:")
        val THIRD_PLACE = EmojiParser.parseToUnicode(":third_place:")
        val MEDAL = EmojiParser.parseToUnicode(":medal:")

        /* Fun */
        /* Game */
        val GAME = EmojiParser.parseToUnicode(":video_game:")
        val HANGED_FACE = EmojiParser.parseToUnicode(":confounded:")
        val EIGHT_BALL = EmojiParser.parseToUnicode(":8ball:")
        /* RPSCommand */
        val ROCK = EmojiParser.parseToUnicode(":last_quarter_moon:")
        val PAPER = EmojiParser.parseToUnicode(":rolled_up_newspaper:")
        val SCISSORS = EmojiParser.parseToUnicode(":scissors:")
        val TIE = EmojiParser.parseToUnicode(":necktie:")

        /* Server Emotes */
        val GUILD_ONLINE = "<:online:313956277808005120>"
        val GUILD_IDLE = "<:away:313956277220802560>"
        val GUILD_DND = "<:dnd:313956276893646850>"
        val GUILD_OFFLINE = "<:offline:313956277237710868>"
        val GUILD_STREAMING = "<:streaming:313956277132853248>"

        val CHECK = "<:check:314349398811475968>"
        val UNCHECK = "<:xmark:314349398824058880>"
        val NOCHECK = "<:empty:314349398723264512>"

        /**
         * Change the String of letters mixed with numbers into a String of emojis
         * @param input the String to be change to emoji
         * @return String of emojis
         */
        fun stringToEmoji(input: String): String {
            var output = ""
            var i = 0
            while (i < input.length) {
                val letters = input.substring(i, i + 1)
                val letterc = input[i]

                /*
            * Number More than ONE digit
            */
                //1234
                if (input.length >= i + 4 && "1234" == input.substring(i, i + 4)) {
                    output += Emoji.NUMBER
                    i += 3
                    i++
                    continue
                }
                //100
                if (input.length >= i + 3 && "100" == input.substring(i, i + 3)) {
                    output += HUNDRED
                    i += 2
                    i++
                    continue
                }

                /*
            * Character more than ONE digit
            */
                //ABCD or ABCD
                if (input.length >= i + 4 && "abcd".equals(input.substring(i, i + 4), ignoreCase = true)) {
                    output += Emoji.ABCD
                    i += 3
                    i++
                    continue
                } else if (input.length >= i + 3 && "abc".equals(input.substring(i, i + 3), ignoreCase = true)) {
                    output += Emoji.ABC
                    i += 2
                    continue
                }//ABC or ABC
                if (input.length >= i + 2 && "vs".equals(input.substring(i, i + 2), ignoreCase = true)) {
                    output += Emoji.VS
                    i += 1
                    i++
                    continue
                }
                if (input.length >= i + 5 && "music".equals(input.substring(i, i + 5), ignoreCase = true)) {
                    output += Emoji.NOTES
                    i += 4
                    i++
                    continue
                }
                if (input.length >= i + 4 && "cool".equals(input.substring(i, i + 4), ignoreCase = true)) {
                    output += Emoji.COOL
                    i += 3
                    i++
                    continue
                }
                if (input.length >= i + 3 && "new".equals(input.substring(i, i + 3), ignoreCase = true)) {
                    output += Emoji.NEW_WORD
                    i += 2
                    i++
                    continue
                }
                if (input.length >= i + 4 && "free".equals(input.substring(i, i + 4), ignoreCase = true)) {
                    output += Emoji.FREE
                    i += 3
                    i++
                    continue
                }
                if (input.length >= i + 2 && "ok".equals(input.substring(i, i + 2), ignoreCase = true)) {
                    output += Emoji.OK
                    i += 1
                    i++
                    continue
                }
                if (input.length >= i + 2 && ".n".equals(input.substring(i, i + 2), ignoreCase = true)) {
                    output += "\n"
                    i += 1
                    i++
                    continue
                }

                /*
            * Check One Letter at a Time
            */
                if (Character.isAlphabetic(letterc.toInt())) {
                    output += lettersToEmoji(letters)
                } else if (Character.isDigit(letterc)) {
                    output += numToEmoji(Integer.parseInt(letters))
                } else if (Character.isWhitespace(letterc)) {
                    output += " "
                } else if (!Character.isAlphabetic(letterc.toInt())) {
                    when (letters) {
                        "." -> output += Emoji.DOT
                        "?" -> output += Emoji.MARK_QUESTION
                        "!" -> output += Emoji.MARK_EXCLAMATION
                        "#" -> output += Emoji.MARK_HASH
                        "*" -> output += Emoji.MARK_ASTERISK
                        "&", "%" -> output += Emoji.SYMBOLS
                        "+" -> output += Emoji.MARK_PLUS_SIGN
                        "-" -> output += Emoji.MARK_MINUS_SIGN
                        "/" -> output += Emoji.MARK_DEVIDE_SIGN
                        else -> {
                        }
                    }
                }//Spacing
                i++
            }
            return output
        }

        /**
         * Change the letter(s) into a String of emojis
         * @param input the letter(s) to be change to emoji
         * @return String of letter(s) in emojis form
         */
        fun lettersToEmoji(input: String): String {
            return ":regional_indicator_" + input.toLowerCase() + ":"
        }

        /**
         * Change the input NUMBER into a String of emojis
         * @param digit the NUMBER to be change to emojis
         * @return String of a NUMBER in emojis from
         */
        fun numToEmoji(digit: Int): String {
            var output = ""
            when (digit) {
                1 -> output += Emoji.ONE
                2 -> output += Emoji.TWO
                3 -> output += Emoji.THREE
                4 -> output += Emoji.FOUR
                5 -> output += Emoji.FIVE
                6 -> output += Emoji.SIX
                7 -> output += Emoji.SEVEN
                8 -> output += Emoji.EIGHT
                9 -> output += Emoji.NINE
                else -> output += Emoji.ZERO
            }

            return output
        }
    }
}
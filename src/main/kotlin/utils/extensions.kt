package utils

import com.jagrosh.jdautilities.command.CommandClient
import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.commons.utils.FinderUtil
import models.GuildSettingsData
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.requests.restaction.MessageAction
import org.jetbrains.exposed.sql.Between
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.asLiteral
import java.io.InputStream
import java.text.NumberFormat
import java.util.*

fun CommandEvent.guildSettings(): GuildSettingsData = this.client.getSettingsFor(guild)

fun CommandEvent.guildSettings(guild: Guild): GuildSettingsData = this.client.getSettingsFor(guild)

fun CommandEvent.findMember(member: String, guild: Guild? = null): Member? {
    val response = FinderUtil.findMembers(member, guild ?: this.guild)
    if (!response.isNullOrEmpty()) {
        return response.first()
    }
    return null
}

fun CommandEvent.findArgMemberOrDefault(): Member? {
    return try {
        if (this.args.isNotBlank()) {
            this.findMember(this.args)
        } else {
            this.member
        }
    } catch (e: Exception) {
        null
    }
}

fun CommandEvent.findTextChannel(textChannel: String, guild: Guild? = null): TextChannel? {
    val response = FinderUtil.findTextChannels(textChannel, guild ?: this.guild)
    if (!response.isNullOrEmpty()) {
        return response.first()
    }
    return null
}

fun CommandEvent.findArgChannelOrDefault(): TextChannel? {
    return try {
        if (this.args.isNotBlank()) {
            this.findTextChannel(this.args)
        } else {
            this.textChannel
        }
    } catch (e: Exception) {
        null
    }
}

fun CommandEvent.findArgGuildOrDefault(): Guild? {
    return try {
        this.jda.getGuildById(this.args) ?: this.jda.getGuildsByName(this.args, true)[0]
    } catch (e: Exception) {
        null
    }
}

fun CommandEvent.findArgCategoryOrDefault(): Category? {
    TODO()
}

fun CommandClient.guildSettingsFor(guild: Guild): GuildSettingsData = this.getSettingsFor(guild)

fun Int.format(locale: Locale = Locale.CANADA): String = NumberFormat.getNumberInstance(locale).format(this)


fun <T, S : T?> ExpressionWithColumnType<S>.betweenTodayAndTomorrow(): Op<Boolean> {
    val between = todayToTomorrow()
    return Between(this, asLiteral(between.first), this.asLiteral(between.second))
}


fun MessageChannel.sendChart(chart: InputStream): MessageAction {
    val embed = EmbedBuilder()
        .setImage("attachment://chart.png")
        .build()
    val message = MessageBuilder()
        .setEmbed(embed)
        .build()
    return this.sendFile(chart, "chart.png", message)
}

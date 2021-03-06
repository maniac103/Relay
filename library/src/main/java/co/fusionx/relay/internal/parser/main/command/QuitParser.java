package co.fusionx.relay.internal.parser.main.command;

import android.util.Pair;

import com.google.common.base.Optional;

import java.util.Collection;
import java.util.List;

import co.fusionx.relay.base.FormatSpanInfo;
import co.fusionx.relay.internal.base.RelayChannel;
import co.fusionx.relay.internal.base.RelayChannelUser;
import co.fusionx.relay.internal.base.RelayQueryUser;
import co.fusionx.relay.internal.base.RelayServer;
import co.fusionx.relay.constants.UserLevel;
import co.fusionx.relay.event.channel.ChannelWorldQuitEvent;
import co.fusionx.relay.event.query.QueryQuitWorldEvent;
import co.fusionx.relay.internal.function.Optionals;
import co.fusionx.relay.util.ParseUtils;
import co.fusionx.relay.util.Utils;

public class QuitParser extends CommandParser {

    private boolean mIsUserQuit;

    public QuitParser(final RelayServer server) {
        super(server);
    }

    @Override
    public void onParseCommand(final List<String> parsedArray, final String prefix) {
        final String nick = ParseUtils.getNickFromPrefix(prefix);
        if (mServer.getUser().isNickEqual(nick)) {
            onQuit();
        } else {
            onUserQuit(parsedArray, nick);
        }
    }

    public boolean isUserQuit() {
        return mIsUserQuit;
    }

    private void onUserQuit(final List<String> parsed, final String userNick) {
        final Optional<RelayChannelUser> optUser = mUserChannelInterface.getUser(userNick);
        Optionals.ifPresent(optUser, user -> {
            final Collection<RelayChannel> channels = mUserChannelInterface.removeUser(user);
            final String reason = parsed.size() == 2 ? parsed.get(1).replace("\"", "") : "";
            final Pair<String, List<FormatSpanInfo>> reasonAndColors =
                    Utils.parseAndStripColorsFromMessage(reason);
            for (final RelayChannel channel : channels) {
                final UserLevel level = user.getChannelPrivileges(channel);
                mUserChannelInterface.removeUserFromChannel(channel, user);
                channel.postAndStoreEvent(new ChannelWorldQuitEvent(channel,
                        user, level, reasonAndColors.first, reasonAndColors.second));
            }
        });

        final Optional<RelayQueryUser> optQuery = mUserChannelInterface.getQueryUser(userNick);
        Optionals.ifPresent(optQuery,
                queryUser -> queryUser.postAndStoreEvent(new QueryQuitWorldEvent(queryUser)));
    }

    private void onQuit() {
        // TODO - improve this
        mIsUserQuit = true;
    }
}
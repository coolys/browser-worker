package io.crawlerbot.crawler.service;

import com.crawler.config.domain.Channel;
import com.github.jsonldjava.utils.JsonUtils;
import io.crawlerbot.crawler.utils.FileConfigUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FrontierServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getChannels() {
        try {

            List<Channel> channelList = FileConfigUtils.getChannels("en-us", "msn-com_us.json");
            System.out.println(JsonUtils.toPrettyString(channelList));
            for(Channel channel: channelList) {
                System.out.println("fetchengine=====" + channel.getConfigFetchEngines());
            }

        }catch (Exception ex) {

        }
    }
}
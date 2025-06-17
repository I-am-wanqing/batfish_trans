package org.batfish.trans;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import org.batfish.common.NetworkSnapshot;
import org.batfish.common.Warnings;
import org.batfish.config.Settings;
import org.batfish.datamodel.ConfigurationFormat;
import org.batfish.identifiers.NetworkId;
import org.batfish.identifiers.SnapshotId;
import org.batfish.job.ParseVendorConfigurationJob;
import org.batfish.job.ParseVendorConfigurationResult;
import org.batfish.representation.cisco.CiscoConfiguration;
import org.batfish.representation.juniper.JuniperConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.batfish.common.util.Resources.readResource;


public final class TransTest {

    private static final String TESTCONFIGS_PREFIX = "org/batfish/grammar/cisco/testconfigs/";
    private static final String JUNIPER_TESTCONFIGS_PREFIX =  "org/batfish/grammar/juniper/testconfigs/";

    private static final String POLICY_TESTCONFIGS = "org/batfish/trans/policytrans/";

    @Rule
    public TemporaryFolder _folder = new TemporaryFolder();

    @Rule public ExpectedException _thrown = ExpectedException.none();

    private static ParseVendorConfigurationResult parseCisco(String resourcePath) {
        return new ParseVendorConfigurationJob(
                new Settings(),
                new NetworkSnapshot(new NetworkId("net"), new SnapshotId("ss")),
                ImmutableMap.of("filename", readResource(resourcePath, UTF_8)),
                new Warnings.Settings(false, false, false),
                ConfigurationFormat.CISCO_IOS,
                ImmutableMultimap.of())
                .call();
    }

    private static ParseVendorConfigurationResult parseJuniper(String resourcePath) {
        return new ParseVendorConfigurationJob(
                new Settings(),
                new NetworkSnapshot(new NetworkId("net"), new SnapshotId("ss")),
                ImmutableMap.of("filename", readResource(resourcePath, UTF_8)),
                new Warnings.Settings(false, false, false),
                ConfigurationFormat.JUNIPER,
                ImmutableMultimap.of())
                .call();
    }


    @Test
    public void testCisco2Juniper(){
        ParseVendorConfigurationResult parseVendorConfigurationResult_cisco = parseCisco(TESTCONFIGS_PREFIX + "as2border1");
        ParseVendorConfigurationResult parseVendorConfigurationResult_juniper = parseJuniper(JUNIPER_TESTCONFIGS_PREFIX + "as2border1");
        CiscoConfiguration vendorConfiguration_cisco = (CiscoConfiguration) parseVendorConfigurationResult_cisco.getVendorConfiguration();
        JuniperConfiguration vendorConfiguration_juniper = (JuniperConfiguration)parseVendorConfigurationResult_juniper.getVendorConfiguration();
        System.out.println(vendorConfiguration_cisco.toString());
        System.out.println(vendorConfiguration_juniper.toString());
        System.out.printf("----- end -----");
    }


    @Test
    public void testPolicyTrans() {
        ParseVendorConfigurationResult parseVendorConfigurationResult_cisco = parseCisco( POLICY_TESTCONFIGS+ "cisco");
        ParseVendorConfigurationResult parseVendorConfigurationResult_juniper = parseJuniper(POLICY_TESTCONFIGS + "juniper.cfg");
        CiscoConfiguration vendorConfiguration_cisco = (CiscoConfiguration) parseVendorConfigurationResult_cisco.getVendorConfiguration();
        JuniperConfiguration vendorConfiguration_juniper = (JuniperConfiguration) parseVendorConfigurationResult_juniper.getVendorConfiguration();
        System.out.println(vendorConfiguration_cisco.toString());
        System.out.println(vendorConfiguration_juniper.toString());
        System.out.printf("----- end -----");
    }

}

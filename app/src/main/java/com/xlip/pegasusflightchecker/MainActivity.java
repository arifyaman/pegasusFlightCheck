package com.xlip.pegasusflightchecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.xlip.pegasusflightchecker.fragment.MyDatePickerFragment;
import com.xlip.pegasusflightchecker.fragment.SelectSavedSearchFragment;
import com.xlip.pegasusflightchecker.httpClient.HttpClient;
import com.xlip.pegasusflightchecker.model.AvailabilityRequest;
import com.xlip.pegasusflightchecker.model.AvailabilityResponse;
import com.xlip.pegasusflightchecker.model.Flight;
import com.xlip.pegasusflightchecker.model.FlightSearchList;
import com.xlip.pegasusflightchecker.model.PortCode;
import com.xlip.pegasusflightchecker.otherActivities.SelectPnrActivity;
import com.xlip.pegasusflightchecker.storage.MyData;
import com.xlip.pegasusflightchecker.storage.MyStorage;
import com.xlip.pegasusflightchecker.storage.SearchObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpClient.HttpClientCallbacks, SelectSavedSearchFragment.SelectSavedSearchFragmentCallback, MyDatePickerFragment.DatePickerCallback {

    private Button sendButton;
    private Button loginButton;
    private Button datePickerButton;
    private Button saveButton;
    private ImageButton dateNextButton;
    private ImageButton dateBeforeButton;
    private ImageButton reverseButton;
    private TextView dateText;
    private TextView responseText;
    private Spinner depPort;
    private Spinner arPort;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout resultLayout;

    private HttpClient httpClient;




    private Gson gson;
    private String depJson = "[{\"ctr\":\"AE\",\"cn\":\"United Arab Emirates\",\"t\":\"Abudhabi\",\"c\":\"AUH\",\"i\":true,\"tg\":[\"AUH\",\"abudabi\",\"ABU DHABI\",\"ABUDABI\",\"auh\",\"birleşik arap emirlikleri\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Adana\",\"c\":\"ADA\",\"i\":false,\"tg\":[\"turkiye\",\"ADA\",\"adana\",\"adna\",\"01\",\"türkiye\",\"ada\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Agri\",\"c\":\"AJI\",\"i\":false,\"tg\":[\"ajı\",\"AJI\",\"AGRI\",\"AĞRI\",\"agri\",\"agrı\",\"ağrı\",\"ağri\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Alanya-Gazipaşa\",\"c\":\"GZP\",\"i\":false,\"tg\":[\"turkiye\",\"GZP\",\"07\",\"gzp\",\"türkiye\",\"alanya\",\"gazipasa\"]},{\"ctr\":\"KZ\",\"cn\":\"Kazakhstan\",\"t\":\"Almati\",\"c\":\"ALA\",\"i\":true,\"tg\":[\"ALA\",\"alma ata\",\"kazakistan\",\"uerup\",\"al\",\"ala\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Amasya - Merzifon\",\"c\":\"MZH\",\"i\":false,\"tg\":[\"05\",\"turkiye\",\"amasya\",\"mzh\",\"merzifon\",\"türkiye\"]},{\"ctr\":\"JO\",\"cn\":\"Jordan\",\"t\":\"Amman\",\"c\":\"AMM\",\"i\":true,\"tg\":[\"Amman\",\"Ürdün\",\"queen\",\"amm\",\"AMM\",\"AMMAN\"]},{\"ctr\":\"NL\",\"cn\":\"Hollanda\",\"t\":\"Amsterdam\",\"c\":\"AMS\",\"i\":true,\"tg\":[\"AMS\",\"HOL\",\"HOLLAND\",\"hol\",\"holland\",\"amsa\",\"hollanda\",\"schiphol\",\"ams\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Ankara\",\"c\":\"ESB\",\"i\":false,\"tg\":[\"turkiye\",\"ankara\",\"06\",\"türkiye\",\"angara\",\"esb\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Antalya\",\"c\":\"AYT\",\"i\":false,\"tg\":[\"turkiye\",\"AYT\",\"07\",\"antalya\",\"türkiye\",\"an\",\"ayt\"]},{\"ctr\":\"GR\",\"cn\":\"Greece\",\"t\":\"Atina\",\"c\":\"ATH\",\"i\":true,\"tg\":[\"ATH\",\"atina\",\"ath\",\"athens\",\"yunanistan\"]},{\"ctr\":\"IQ\",\"cn\":\"Iraq\",\"t\":\"Bağdat\",\"c\":\"BGW\",\"i\":true,\"tg\":[\"bgw\",\"ırak\",\"BGW\",\"irak\",\"bagdat\",\"baghdad\",\"bgwa\"]},{\"ctr\":\"BH\",\"cn\":\"Bahrain\",\"t\":\"Bahreyn\",\"c\":\"BAH\",\"i\":true,\"tg\":[\"BAH\",\"bah\",\"bahrain\",\"bahreyn\"]},{\"ctr\":\"AZ\",\"cn\":\"Azerbaijan\",\"t\":\"Bakü\",\"c\":\"GYD\",\"i\":true,\"tg\":[\"bakü\",\"azerbaycan\",\"gyd\",\"GYD\",\"BAKU\",\"BAKÜ\",\"AZERBAYCAN\",\"haydar aliyev\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Balıkesir-Edremit\",\"c\":\"EDO\",\"i\":false,\"tg\":[\"turkiye\",\"EDO\",\"edo\",\"balikesir\",\"türkiye\",\"edremit\",\"10\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Bari\",\"c\":\"BRI\",\"i\":false},{\"ctr\":\"ES\",\"cn\":\"Spain\",\"t\":\"Barselona\",\"c\":\"BCN\",\"i\":true,\"tg\":[\"SPA\",\"SPAIN\",\"spain\",\"spa\",\"BAR\",\"BARCELONA\",\"BCN\",\"barcelona\",\"bcn\",\"messi\",\"barca\",\"ispanya\",\"barce\"]},{\"ctr\":\"CH\",\"cn\":\"Switzerland\",\"t\":\"Basel - Mulhouse\",\"c\":\"BSL\",\"i\":true,\"tg\":[\"BSL\",\"basel\",\"bsl\",\"isviçre\",\"swiss\"]},{\"ctr\":\"IQ\",\"cn\":\"Iraq\",\"t\":\"Basra\",\"c\":\"BSR\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Batman\",\"c\":\"BAL\",\"i\":false,\"tg\":[\"turkiye\",\"BAL\",\"72\",\"bal\",\"ba\",\"türkiye\",\"batman\"]},{\"ctr\":\"RS\",\"cn\":\"Serbia\",\"t\":\"Belgrad\",\"c\":\"BEG\",\"i\":true,\"tg\":[\"beg\",\"BEG\",\"beograd\",\"sırbistan\",\"serbia\",\"sırp\",\"belgırad\"]},{\"ctr\":\"NO\",\"cn\":\"Norway\",\"t\":\"Bergen\",\"c\":\"BGO\",\"i\":false},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Berlin-Schönefeld\",\"c\":\"SXF\",\"i\":true,\"tg\":[\"GERMANY\",\"germany\",\"ALMANYA\",\"berlin\",\"BERLIN\",\"ger\",\"GER\",\"SXF\",\":de:\",\"schoenefeld\",\"sxf\",\"sc\",\"almanya\"]},{\"ctr\":\"LB\",\"cn\":\"Lebanon\",\"t\":\"Beyrut\",\"c\":\"BEY\",\"i\":true,\"tg\":[\"lubnan\",\"beyrut\",\"beirut\",\"BEY\",\"uorup\",\"beyrat\",\"be\",\"beyrüt\",\"lübnan\",\"lbnan\",\"bey\",\"\"]},{\"ctr\":\"DK\",\"cn\":\"Denmark\",\"t\":\"Billund\",\"c\":\"BLL\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Bingöl\",\"c\":\"BGG\",\"i\":false,\"tg\":[\"bgg\",\"BGG\",\"BINGOL\",\"BİNGÖL\",\"bıngol\",\"bingöl\",\"bin\",\"BIN\",\"BING\",\"bing\"]},{\"ctr\":\"KG\",\"cn\":\"Kyrgyzstan\",\"t\":\"Bişkek\",\"c\":\"FRU\",\"i\":true,\"tg\":[\"FRU\",\"uorup\",\"kırgızistan\",\"bi\",\"fru\",\"bişkek\",\"biskek\",\"bskek\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Bodrum\",\"c\":\"BJV\",\"i\":false,\"tg\":[\"turkiye\",\"BJV\",\"48\",\"türkiye\",\"bjv\",\"muğla\",\"mugla\",\"bordum\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Bologna\",\"c\":\"BLQ\",\"i\":true,\"tg\":[\"bolonya\",\"italya\",\"ITA\",\"ita\",\"ITALY\",\"italy\",\"BOL\",\"BOLOGNA\",\"BLQ\",\"bo\",\"uboup\",\"bologna\",\"blq\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Bremen\",\"c\":\"BRE\",\"i\":false},{\"ctr\":\"BE\",\"cn\":\"Belgium\",\"t\":\"Brüksel\",\"c\":\"BRU\",\"i\":false},{\"ctr\":\"BE\",\"cn\":\"Belgium\",\"t\":\"Brüksel-Charleroi\",\"c\":\"CRL\",\"i\":true,\"tg\":[\"BRU\",\"CRL\",\"CHARLEROI\",\"Charleroi\",\"crl\",\"belgium\",\"brüksel\",\"br\"]},{\"ctr\":\"HU\",\"cn\":\"Hungary\",\"t\":\"Budapeşte\",\"c\":\"BUD\",\"i\":true,\"tg\":[\"budapeşte\",\"BUD\",\"macaristan\",\"bud\",\"peste\",\"buda\",\"budegy\",\"ferihegy\",\"peşte\",\"bu\"]},{\"ctr\":\"RO\",\"cn\":\"Roumania\",\"t\":\"Bükreş-Otopeni\",\"c\":\"OTP\",\"i\":true,\"tg\":[\"OTP\",\"otpeni\",\"bükreş\",\"otp\",\"otapeni\",\"bu\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Catania\",\"c\":\"CTA\",\"i\":false},{\"ctr\":\"CH\",\"cn\":\"Switzerland\",\"t\":\"Cenevre\",\"c\":\"GVA\",\"i\":true,\"tg\":[\"ceneva\",\"cenevre\",\"geneva\",\"ge\",\"gva\",\"isviçre\",\"swiss\"]},{\"ctr\":\"SA\",\"cn\":\"Saudi Arabia\",\"t\":\"Cidde\",\"c\":\"JED\",\"i\":true,\"tg\":[\"cidde\",\"Cidde\",\"Jed\",\"Suudi\",\"Saudi\",\"Arab\",\"KING\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Dalaman\",\"c\":\"DLM\",\"i\":false,\"tg\":[\"turkiye\",\"48\",\"da\",\"türkiye\",\"fethiye\",\"muğla\",\"marmaris\",\"dlm\",\"mugla\",\"dalaman\"]},{\"ctr\":\"SA\",\"cn\":\"Saudi Arabia\",\"t\":\"Dammam\",\"c\":\"DMM\",\"i\":true,\"tg\":[\"Suudi\",\"Saudi\",\"Arab\",\"DMM\",\"damman\",\"suudi arabistan\",\"saudi arabia\",\"dmm\",\"kral fahd\",\"dam\",\"arabis\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Denizli\",\"c\":\"DNZ\",\"i\":false,\"tg\":[\"turkiye\",\"DNZ\",\"dnzli\",\"türkiye\",\"denzli\",\"den\",\"20\",\"dnz\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Diyarbakır\",\"c\":\"DIY\",\"i\":false,\"tg\":[\"turkiye\",\"DIY\",\"DİY\",\"diyarbekir\",\"21\",\"türkiye\",\"dyrbkr\",\"dıy\",\"diy\"]},{\"ctr\":\"QA\",\"cn\":\"Qatar\",\"t\":\"Doha\",\"c\":\"DOH\",\"i\":true,\"tg\":[\"DOH\",\"doh\",\"dho\",\"katar\",\"doho\"]},{\"ctr\":\"AE\",\"cn\":\"United Arab Emirates\",\"t\":\"Dubai Tümü\",\"c\":\"DXB_SHJ\",\"i\":false},{\"ctr\":\"AE\",\"cn\":\"United Arab Emirates\",\"t\":\"Dubai-Dubai\",\"c\":\"DXB\",\"i\":true,\"tg\":[\"DXB\",\"sharjah\",\"şarjah\",\"birleşik arap emirlikleri\",\"dubai\",\"dubaı\",\"bae\",\"dxb\",\"du\",\"uduup\",\"\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Düsseldorf\",\"c\":\"DUS\",\"i\":true,\"tg\":[\"düseldorf\",\"GERMANY\",\"germany\",\"DUSSELDORF\",\"dusseldorf\",\"DÜSSELDORF\",\"düsseldorf\",\"ger\",\"GER\",\"DUS\",\"düzeldorf\",\"duesseldorf\",\"almanya\",\"dus\"]},{\"ctr\":\"NL\",\"cn\":\"Hollanda\",\"t\":\"Eindhoven\",\"c\":\"EIN\",\"i\":true,\"tg\":[\"EIN\",\"ein\",\"NEDERLANS\",\"Hollanda\",\"Eindhoven\",\"hollanda\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Elazığ\",\"c\":\"EZS\",\"i\":false,\"tg\":[\"turkiye\",\"EZS\",\"ezs\",\"ez\",\"elazıg\",\"elazig\",\"türkiye\",\"elaziğ\",\"23\",\"elezoğ\"]},{\"ctr\":\"IQ\",\"cn\":\"Iraq\",\"t\":\"Erbil\",\"c\":\"EBL\",\"i\":true,\"tg\":[\"ırak\",\"EBL\",\"ebl\",\"irak\",\"er\",\"erbil\",\"uerup\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Erfurt\",\"c\":\"ERF\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Erzincan\",\"c\":\"ERC\",\"i\":false,\"tg\":[\"turkiye\",\"ERC\",\"erzincan\",\"24\",\"erc\",\"türkiye\",\"erzzncan\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Erzurum\",\"c\":\"ERZ\",\"i\":false,\"tg\":[\"turkiye\",\"er\",\"25\",\"erz\",\"türkiye\",\"erzurum\",\"palandöken\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Frankfurt - Main\",\"c\":\"FRA\",\"i\":true,\"tg\":[\"GERMANY\",\"germany\",\"ger\",\"GER\",\"fra\",\"franfurt\",\"almanya\",\"frankfurt\",\"alamanya\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Gaziantep\",\"c\":\"GZT\",\"i\":false,\"tg\":[\"turkiye\",\"GZT\",\"baklava\",\"antep\",\"gzt\",\"türkiye\",\"gazıantep\",\"27\",\"anteb\"]},{\"ctr\":\"PL\",\"cn\":\"Poland\",\"t\":\"Gdansk\",\"c\":\"GDN\",\"i\":false},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Genoa\",\"c\":\"GOA\",\"i\":false},{\"ctr\":\"SE\",\"cn\":\"Sweden\",\"t\":\"Göteborg - Landvetter\",\"c\":\"GOT\",\"i\":false},{\"ctr\":\"RU\",\"cn\":\"Russia\",\"t\":\"Grozny\",\"c\":\"GRV\",\"i\":true,\"tg\":[\"Gro\",\"Grozny\",\"grozni\",\"rusya\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Hamburg\",\"c\":\"HAM\",\"i\":true,\"tg\":[\"GERMANY\",\"germany\",\"ger\",\"GER\",\"HAM\",\"hamburg\",\"hama\",\"almanya\",\"hamgurg\",\"ham\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Hannover\",\"c\":\"HAJ\",\"i\":true,\"tg\":[\"HANNOVER\",\"hannover\",\"almanya\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Hatay\",\"c\":\"HTY\",\"i\":false,\"tg\":[\"turkiye\",\"HYT\",\"türkiye\",\"ha\",\"hatay\",\"uhaup\",\"hty\",\"31\"]},{\"ctr\":\"GR\",\"cn\":\"Greece\",\"t\":\"Heraklion\",\"c\":\"HER\",\"i\":false},{\"ctr\":\"EG\",\"cn\":\"Egypt\",\"t\":\"Hurgada\",\"c\":\"HRG\",\"i\":true,\"tg\":[\"HRG\",\"hurghada\",\"hrg\",\"mısır\",\"misir\",\"egtpy\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Istanbul Airport\",\"c\":\"IST\",\"i\":false,\"tg\":[\"istanbul\",\"turkiye\",\"türkiye\",\"ata\",\"34\",\"ist\",\"istambul\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Istanbul Tümü\",\"c\":\"IST_SAW\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Istanbul-S.Gökçen\",\"c\":\"SAW\",\"i\":false,\"tg\":[\"saw\",\"istanbul\",\"gökçen\",\"turkiye\",\"SAW\",\"sabiha\",\"sabiş\",\"türkiye\",\"34\",\"gokcen\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Izmir\",\"c\":\"ADB\",\"i\":false,\"tg\":[\"turkiye\",\"türkiye\",\"adb\",\"izmr\",\"35\"]},{\"ctr\":\"EG\",\"cn\":\"Egypt\",\"t\":\"Kahire\",\"c\":\"CAI\",\"i\":true,\"tg\":[\"Kahire\",\"CAI\",\"cai\",\"cairo\",\"kah\",\"kahi\",\"Cairo\",\"CAIRO\",\"CAIR\",\"KAH\",\"KAHIRE\",\"KAHİRE\",\"mısır\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Kahramanmaraş\",\"c\":\"KCM\",\"i\":false,\"tg\":[\"turkiye\",\"KCM\",\"maras\",\"türkiye\",\"46\",\"maraş\",\"kcm\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Kars\",\"c\":\"KSY\",\"i\":false,\"tg\":[\"turkiye\",\"KSY\",\"kars\",\"türkiye\",\"ksy\",\"36\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Kayseri\",\"c\":\"ASR\",\"i\":false,\"tg\":[\"turkiye\",\"ASR\",\"kyseri\",\"kaysri\",\"türkiye\",\"asr\",\"gayseri\",\"38\"]},{\"ctr\":\"MA\",\"cn\":\"Morocco\",\"t\":\"Kazablanka\",\"c\":\"CMN\",\"i\":true,\"tg\":[\"CMN\",\"cmn\",\"kazablanka\",\"fas\",\"morocco\",\"casablanca\"]},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Kharkiv\",\"c\":\"HRK\",\"i\":true,\"tg\":[\"HRK\",\"ukrayna\",\"karkiv\",\"harkiv\",\"hrk\"]},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Kiev Tümü\",\"c\":\"IEV_KBP\",\"i\":false},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Kiev-Borispol\",\"c\":\"KBP\",\"i\":true,\"tg\":[\"kiev\",\"BORYSPIL\",\"kbp\",\"KBP\",\"ukraine\",\"boryspil\",\"ukrayna\"]},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Kiev-Zhulyany\",\"c\":\"IEV\",\"i\":true,\"tg\":[\"iev\",\"IEV\",\"kiev\",\"dinamo\",\"dynamo\",\"ukrayna\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Konya\",\"c\":\"KYA\",\"i\":false,\"tg\":[\"kenya\",\"turkiye\",\"KYA\",\"knya\",\"türkiye\",\"42\",\"kya\"]},{\"ctr\":\"DK\",\"cn\":\"Denmark\",\"t\":\"Kopenhag\",\"c\":\"CPH\",\"i\":true,\"tg\":[\"KOPENHAG\",\"CPH\",\"uorup\",\"copenhagen\",\"cph\",\"co\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Köln\",\"c\":\"CGN\",\"i\":true,\"tg\":[\"GERMANY\",\"germany\",\"ger\",\"GER\",\"CGN\",\"bonn\",\"köln\",\"cologne\",\"koln\",\"almanya\",\"alamanya\",\"koeln\"]},{\"ctr\":\"RU\",\"cn\":\"Russia\",\"t\":\"Krasnodar\",\"c\":\"KRR\",\"i\":true,\"tg\":[\"KRR\",\"kr\",\"uerup\",\"rusya\",\"krasnodar\",\"krr\"]},{\"ctr\":\"KW\",\"cn\":\"Kuwait\",\"t\":\"Kuveyt\",\"c\":\"KWI\",\"i\":true,\"tg\":[\"kwı\",\"küveyt\",\"kuveyt\",\"kwi\"]},{\"ctr\":\"CP\",\"cn\":\"K.K.T.C.\",\"t\":\"Lefkoşa - Kuzey Kıbrıs\",\"c\":\"ECN\",\"i\":true,\"tg\":[\"ECN\",\"ecn\",\"nicosia\",\"north\",\"Kıbrıs\",\"Kuzey Kıbrıs\",\"Lefkoşa\",\"Lefkoşe\",\"cyprus\",\"ercan\",\"kuzey\",\"kipris\",\"kktc\",\"kibris\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Leipzig\",\"c\":\"LEJ\",\"i\":true},{\"ctr\":\"GB\",\"cn\":\"United Kingdom\",\"t\":\"Londra Tümü\",\"c\":\"STN_LHR_LGW\",\"i\":false},{\"ctr\":\"GB\",\"cn\":\"United Kingdom\",\"t\":\"Londra-Gatwick\",\"c\":\"LGW\",\"i\":true,\"tg\":[\"Londra\",\"London\",\"ENG\",\"ENGLAND\",\"lon\",\"LON\",\"LONDON\",\"LGW\",\"ingiltere\",\"lgw\",\"england\",\"groion\",\"gatwick\"]},{\"ctr\":\"GB\",\"cn\":\"United Kingdom\",\"t\":\"Londra-Stansted\",\"c\":\"STN\",\"i\":true,\"tg\":[\"Londra\",\"London\",\"ENG\",\"ENGLAND\",\"lon\",\"LON\",\"LONDON\",\"STN\",\"ingiltere\",\"stn\",\"england\",\"standsted\"]},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Lviv\",\"c\":\"LWO\",\"i\":true,\"tg\":[\"LWO\",\"lbvov\",\"lvıv\",\"lwow\",\"ukrayna\",\"livov\",\"lwo\",\"lemberg\",\"\"]},{\"ctr\":\"FR\",\"cn\":\"France\",\"t\":\"Lyon-Saint Exupery\",\"c\":\"LYS\",\"i\":true,\"tg\":[\"lüyon\",\"FRA\",\"france\",\"FRANCE\",\"LYON\",\"LYO\",\"lyo\",\"LYS\",\"fra\",\"lys\",\"lyon\",\"fransa\"]},{\"ctr\":\"ES\",\"cn\":\"Spain\",\"t\":\"Madrid - Barajas\",\"c\":\"MAD\",\"i\":true,\"tg\":[\"SPA\",\"SPAIN\",\"spain\",\"spa\",\"ronaldo\",\"MAD\",\"ıspanya\",\"ma\",\"mad\",\"madrıd\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Malatya\",\"c\":\"MLX\",\"i\":false,\"tg\":[\"turkiye\",\"MLX\",\"malatya\",\"türkiye\",\"maltya\",\"mlx\",\"44\"]},{\"ctr\":\"GB\",\"cn\":\"United Kingdom\",\"t\":\"Manchester\",\"c\":\"MAN\",\"i\":true,\"tg\":[\"MAN\",\"man\",\"Manchester\",\"ingiltere\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Mardin\",\"c\":\"MQM\",\"i\":false,\"tg\":[\"turkiye\",\"mardin\",\"47\",\"türkiye\",\"mardn\",\"mqm\"]},{\"ctr\":\"FR\",\"cn\":\"France\",\"t\":\"Marsilya\",\"c\":\"MRS\",\"i\":true,\"tg\":[\"FRA\",\"france\",\"FRANCE\",\"MRS\",\"fra\",\"marseille\",\"mrs\",\"fransa\"]},{\"ctr\":\"OM\",\"cn\":\"Oman\",\"t\":\"Maskat\",\"c\":\"MCT\",\"i\":true,\"tg\":[\"Muskat\",\"Muscat\",\"Maskat\",\"MCT\",\"maskat\",\"mct\",\"mascat\",\"MASKAT\",\"umman\",\"UMMAN\",\"muska\",\"musk\",\"mus\"]},{\"ctr\":\"SA\",\"cn\":\"Saudi Arabia\",\"t\":\"Medina\",\"c\":\"MED\",\"i\":false},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Milan-Bergamo\",\"c\":\"BGY\",\"i\":true,\"tg\":[\"Milano\",\"italya\",\"ITA\",\"ita\",\"ITALY\",\"italy\",\"mil\",\"BGY\",\"orio\",\"bgy\",\"serio\"]},{\"ctr\":\"RU\",\"cn\":\"Russia\",\"t\":\"Mineralnye Vody\",\"c\":\"MRV\",\"i\":true,\"tg\":[\"MRV\",\"MINERALNYE\",\"rusya\",\"mrv\"]},{\"ctr\":\"RU\",\"cn\":\"Russia\",\"t\":\"Moskova - Domodedovo\",\"c\":\"DME\",\"i\":true,\"tg\":[\"Moscow\",\"DME\",\"dme\",\"moskva\",\"mosa\",\"domododevo\",\"rusya\",\"maskva\",\"\"]},{\"ctr\":\"RU\",\"cn\":\"Russia\",\"t\":\"Moskova Tümü\",\"c\":\"DME_ZIA\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Muş\",\"c\":\"MSR\",\"i\":false,\"tg\":[\"MUŞ\",\"MSR\",\"mus\",\"msr\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Münih\",\"c\":\"MUC\",\"i\":true,\"tg\":[\"GERMANY\",\"germany\",\"ger\",\"GER\",\"MUC\",\"muenchen\",\"almanya\",\"munich\",\"alamanya\",\"münih\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Münster\",\"c\":\"FMO\",\"i\":false},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Napoli\",\"c\":\"NAP\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Nevşehir\",\"c\":\"NAV\",\"i\":false},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Nürnberg\",\"c\":\"NUE\",\"i\":true,\"tg\":[\"nünberk\",\"GERMANY\",\"germany\",\"NÜRENBERG\",\"nürenberg\",\"ger\",\"GER\",\"NUE\",\"nunberg\",\"nünberg\",\"nü\",\"nue\",\"almanya\",\"uerup\",\"nürünberg\",\"nürnberg\",\"nurunberg\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Ordu-Giresun\",\"c\":\"OGU\",\"i\":false,\"tg\":[\"turkiye\",\"OGU\",\"GİRESUN\",\"gire\",\"uorup\",\"türkiye\",\"or\",\"ordu\",\"ogu\",\"52\"]},{\"ctr\":\"NO\",\"cn\":\"Norway\",\"t\":\"Oslo\",\"c\":\"OSL\",\"i\":true,\"tg\":[\"OSL\",\"osla\",\"norway\",\"osl\",\"norvec\",\"norveç\"]},{\"ctr\":\"KG\",\"cn\":\"Kyrgyzstan\",\"t\":\"Oş\",\"c\":\"OSS\",\"i\":true,\"tg\":[\"oş\",\"osh\",\"OSS\",\"oss\",\"kırgızistan\",\"os\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Paderborn/Lippstadt\",\"c\":\"PAD\",\"i\":false},{\"ctr\":\"ES\",\"cn\":\"Spain\",\"t\":\"Palma\",\"c\":\"PMI\",\"i\":false},{\"ctr\":\"ES\",\"cn\":\"Spain\",\"t\":\"Pamplona\",\"c\":\"PNA\",\"i\":false},{\"ctr\":\"FR\",\"cn\":\"France\",\"t\":\"Paris Tümü\",\"c\":\"CDG_ORY\",\"i\":false},{\"ctr\":\"FR\",\"cn\":\"France\",\"t\":\"Paris-Charles de Gaulle\",\"c\":\"CDG\",\"i\":true,\"tg\":[\"paris\",\"PARIS\",\"par\",\"CDG\",\"Charles de Gaulle \",\"Charles\",\"cdg\",\"pars\",\"cha\",\"fransa\"]},{\"ctr\":\"FR\",\"cn\":\"France\",\"t\":\"Paris-Orly\",\"c\":\"ORY\",\"i\":true,\"tg\":[\"FRA\",\"france\",\"FRANCE\",\"paris\",\"PARIS\",\"PAR\",\"par\",\"ORY\",\"fra\",\"pari\",\"orly\",\"ory\",\"fransa\"]},{\"ctr\":\"PL\",\"cn\":\"Poland\",\"t\":\"Poznan\",\"c\":\"POZ\",\"i\":false},{\"ctr\":\"CZ\",\"cn\":\"Czechoslovakia\",\"t\":\"Prag\",\"c\":\"PRG\",\"i\":true,\"tg\":[\"PRG\",\"çekya\",\"prg\",\"czech\",\"prague\",\"uerup\",\"çek cumhuriyeti\",\"pr\",\"check\",\"ruzyne\"]},{\"ctr\":\"RS\",\"cn\":\"Serbia\",\"t\":\"Priştine\",\"c\":\"PRN\",\"i\":true,\"tg\":[\"piriştina\",\"PRN\",\"pırıştına\",\"prn\",\"piristine\",\"pr\",\"priştine\",\"kosova\",\"uprup\"]},{\"ctr\":\"SA\",\"cn\":\"Saudi Arabia\",\"t\":\"Riyad\",\"c\":\"RUH\",\"i\":true,\"tg\":[\"Suudi\",\"Arab\",\"RUH\",\"Riyad\",\"king khalid\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Roma-Fiumicino\",\"c\":\"FCO\",\"i\":true,\"tg\":[\"roma\",\"rome\",\"fco\",\"italya\",\"ROME\",\"ITA\",\"ita\",\"ITALY\",\"italy\",\"FCO\",\"fiumicino\"]},{\"ctr\":\"NL\",\"cn\":\"Hollanda\",\"t\":\"Rotterdam\",\"c\":\"RTM\",\"i\":true,\"tg\":[\"RTM\",\"rtm\",\"ROTTERDAM\",\"rotterdam\",\"netherlands\",\"hollanda\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Samsun\",\"c\":\"SZF\",\"i\":false,\"tg\":[\"samnsun\",\"turkiye\",\"SZF\",\"türkiye\",\"55\",\"smsun\",\"szf\"]},{\"ctr\":\"BA\",\"cn\":\"Bosna And Herzegovina\",\"t\":\"Saraybosna\",\"c\":\"SJJ\",\"i\":true,\"tg\":[\"bosna\",\"usaup\",\"sjj\",\"sa\",\"hersek\",\"saraybosna\",\"bosnia\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Sinop\",\"c\":\"NOP\",\"i\":false,\"tg\":[\"SİNOP\",\"sinop\",\"SINOP\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Sirnak\",\"c\":\"NKT\",\"i\":false,\"tg\":[\"nkt\",\"NKT\",\"şırnak\",\"Şırnak\",\"sirnak\",\"Şerafettin Elçi\",\"ŞIRNAK\",\"sırnak\",\"şır\",\"sir\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Sivas\",\"c\":\"VAS\",\"i\":false,\"tg\":[\"turkiye\",\"sivas\",\"vas\",\"58\",\"türkiye\",\"sivs\"]},{\"ctr\":\"SE\",\"cn\":\"Sweden\",\"t\":\"Stockholm - Arlanda\",\"c\":\"ARN\",\"i\":true,\"tg\":[\"arn\",\"ARN\",\"tokholm\",\"arlanda\",\"isveç\",\"isvec\",\"stkholm\",\"holm\",\"\"]},{\"ctr\":\"DE\",\"cn\":\"Germany\",\"t\":\"Stuttgart\",\"c\":\"STR\",\"i\":true,\"tg\":[\"şututgart\",\"GERMANY\",\"germany\",\"ger\",\"GER\",\"STR\",\"echterdingen\",\"stutgart\",\"str\",\"almanya\",\"alamanya\",\"sututgart\",\"ştutgart\"]},{\"ctr\":\"IQ\",\"cn\":\"Iraq\",\"t\":\"Süleymaniye\",\"c\":\"ISU\",\"i\":true,\"tg\":[\"ırak\",\"IRAK\",\"süleymaniye\",\"SULEYMANIYE\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Şanlıurfa\",\"c\":\"GNY\",\"i\":false,\"tg\":[\"turkiye\",\"GNY\",\"urfa\",\"türkiye\",\"şanliurfa\",\"gny\",\"63\"]},{\"ctr\":\"AE\",\"cn\":\"United Arab Emirates\",\"t\":\"Şarjah\",\"c\":\"SHJ\",\"i\":true,\"tg\":[\"sharjah\",\"şarika\",\"sharhaj\",\"SHJ\",\"ŞARİKA\",\"birleşik arap emirlikleri\",\"dubai\",\"Dubai-Tümü\",\"dubaı\",\"bae\"]},{\"ctr\":\"EG\",\"cn\":\"Egypt\",\"t\":\"Şarm El-Şeyh\",\"c\":\"SSH\",\"i\":true,\"tg\":[\"şey\",\"şarm\",\"şeyh\",\"el\",\"ssh\",\"şarmenşey\",\"mısır\",\"misir\"]},{\"ctr\":\"IR\",\"cn\":\"Iran\",\"t\":\"Tahran - IKA\",\"c\":\"IKA\",\"i\":true,\"tg\":[\"IKA\",\"İKA\",\"ika\",\"tahiran\",\"iran\",\"ıka\"]},{\"ctr\":\"EE\",\"cn\":\"Estonia\",\"t\":\"Tallinn\",\"c\":\"TLL\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Tekirdağ - Çorlu\",\"c\":\"TEQ\",\"i\":false,\"tg\":[\"tekirdağ\",\"çorlu\",\"TEKİRDAĞ\",\"ÇORLU\"]},{\"ctr\":\"IL\",\"cn\":\"Israel\",\"t\":\"Tel Aviv - Ben Gurion\",\"c\":\"TLV\",\"i\":true,\"tg\":[\"TLV\",\"tlv\",\"telaviv\",\"israil\",\"groion\"]},{\"ctr\":\"GE\",\"cn\":\"Georgia\",\"t\":\"Tiflis\",\"c\":\"TBS\",\"i\":true,\"tg\":[\"tbs\",\"tiflis\",\"gürcistan\"]},{\"ctr\":\"AL\",\"cn\":\"Albania\",\"t\":\"Tiran\",\"c\":\"TIA\",\"i\":true,\"tg\":[\"TIA\",\"TİA\",\"tirana\",\"arnavutluk\",\"tia\",\"rinas\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Trabzon\",\"c\":\"TZX\",\"i\":false,\"tg\":[\"turkiye\",\"TZX\",\"ts\",\"trabzan\",\"61\",\"türkiye\",\"tzx\",\"\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Turin\",\"c\":\"TRN\",\"i\":false},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Uşak\",\"c\":\"USQ\",\"i\":false,\"tg\":[\"USQ\",\"usq\",\"usak\",\"UŞAK\",\"USAK\",\"uşak\",\"uş\"]},{\"ctr\":\"MK\",\"cn\":\"Macedonia\",\"t\":\"Üsküp\",\"c\":\"SKP\",\"i\":true,\"tg\":[\"SKP\",\"makedonya\",\"skp\",\"sk\",\"üsküp\",\"uskup\"]},{\"ctr\":\"TR\",\"cn\":\"Turkey\",\"t\":\"Van\",\"c\":\"VAN\",\"i\":false,\"tg\":[\"turkiye\",\"VAN\",\"van\",\"va\",\"türkiye\",\"one\",\"65\"]},{\"ctr\":\"IT\",\"cn\":\"Italy\",\"t\":\"Venedik\",\"c\":\"VCE\",\"i\":true,\"tg\":[\"italya\",\"ITALY\",\"VCE\",\"marco polo\",\"Venedik\",\"venice\",\"VENICE\"]},{\"ctr\":\"AT\",\"cn\":\"Austria\",\"t\":\"Viyana\",\"c\":\"VIE\",\"i\":true,\"tg\":[\"vie\",\"VIE\",\"VİE\",\"vıe\",\"avusturya\",\"wi\",\"vien\",\"wien\",\"viyana\",\"vienna\"]},{\"ctr\":\"UA\",\"cn\":\"Ukranie\",\"t\":\"Zaporijya\",\"c\":\"OZH\",\"i\":true,\"tg\":[\"ukranya\",\"ukrayna\",\"zaparizhia\",\"ozha\",\"zapa\",\"ozh\",\"zaparojni\"]},{\"ctr\":\"CH\",\"cn\":\"Switzerland\",\"t\":\"Zürih\",\"c\":\"ZRH\",\"i\":true,\"tg\":[\"zrh\",\"kl\",\"zürih\",\"isviçte\",\"kloten\",\"swiss\"]}]";
    ArrayList<PortCode> portCodes;
    List<String> codeArray;
    String depPortText = "DOH";
    String arPortText = "IST_SAW";

    private MyDatePickerFragment myDatePickerFragment;
    private SelectSavedSearchFragment selectSavedSearchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.httpClient = new HttpClient(this);
        gson = new Gson();

        new Thread(() -> {
            HttpClient.getJson("https://web.flypgs.com/pegasus/app-init", "init", this);
        }).start();


        portCodes = gson.fromJson(depJson, new TypeToken<List<PortCode>>() {
        }.getType());


        codeArray = new ArrayList<>();
        for (PortCode port: portCodes) {
            codeArray.add(port.getT() + " - " + port.getC());

        }

        depPort = findViewById(R.id.depPort);
        arPort = findViewById(R.id.arPort);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, codeArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        resultLayout = findViewById(R.id.scrl_search_result);

        depPort.setAdapter(spinnerArrayAdapter);
        depPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depPortText = portCodes.get(position).getC();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        depPort.setSelection(41);


        arPort.setAdapter(spinnerArrayAdapter);
        arPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arPortText = portCodes.get(position).getC();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        arPort.setSelection(64);

        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener((View v) -> {
            myDatePickerFragment.show(getSupportFragmentManager(), "date picker");
        });


        saveButton = findViewById(R.id.saveButton);
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setOnClickListener((View view) -> {
            saveMyData();
        });

        dateText = findViewById(R.id.dateText);
        responseText = findViewById(R.id.responseText);


        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener((View v) -> {
            search();
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener((View v) -> {
            login();
        });

        dateBeforeButton = findViewById(R.id.date_previous_button);
        dateBeforeButton.setOnClickListener((View v) -> {
            myDatePickerFragment.oneDayBefore();
        });

        reverseButton = findViewById(R.id.reverse_button);
        reverseButton.setOnClickListener((View v) -> {
            int temp = depPort.getSelectedItemPosition();
            depPort.setSelection(arPort.getSelectedItemPosition());
            arPort.setSelection(temp);

        });

        dateNextButton = findViewById(R.id.date_next_button);
        dateNextButton.setOnClickListener((View v) -> {
            myDatePickerFragment.oneDayNext();
        });

        setUpFragments();
    }

    private void search() {
        saveButton.setVisibility(View.INVISIBLE);
        responseText.setText("Loading ...");
        AvailabilityRequest request = createAvailabilityReqeust(dateText.getText().toString(), depPortText, arPortText);
        String payload = gson.toJson(request);
        new Thread(() -> {
            httpClient.postJson("https://web.flypgs.com/pegasus/availability", payload, "searchList", MainActivity.this);
        }).start();
    }

    private void login() {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserName", "905433214929");
        params.put("Password", "785666");
        params.put("Mask", "+90(###)###-####");
        params.put("Country", "TR");
        params.put("RememberMe", "false");
        params.put("DeviceType", "Desktop");
        params.put("ScreenCode", "1000");

        new Thread(() -> {
            httpClient.postForm("https://web.flypgs.com/pegasus/availability", params, "login", MainActivity.this);
        }).start();
    }


    private void setUpFragments() {
        myDatePickerFragment = new MyDatePickerFragment();
        myDatePickerFragment.setDatePickerCallback(this);
        selectSavedSearchFragment = new SelectSavedSearchFragment();
        selectSavedSearchFragment.setCallback(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectSavedSearchFragment.show(getSupportFragmentManager(), "selectSavedSearchFragment");
        return super.onOptionsItemSelected(item);
    }


    private void saveMyData() {

        MyData data = MyStorage.getData(getApplicationContext());
        if (data == null) {
            data = new MyData();
        }
        SearchObject searchObject = new SearchObject();
        searchObject.setDate(myDatePickerFragment.getCalendar());
        searchObject.setArPort(((int) arPort.getSelectedItemId()));
        searchObject.setDepPort(((int) depPort.getSelectedItemId()));
        searchObject.setId(System.currentTimeMillis());
        searchObject.setTitle(depPortText + " - " + arPortText + " (" + dateText.getText().toString() + ")");
        data.getSearchObjects().add(searchObject);

        MyStorage.saveData(getApplicationContext(), data);
    }


    private AvailabilityRequest createAvailabilityReqeust(String date, String arrivalPort, String departurePort) {
        AvailabilityRequest request = new AvailabilityRequest();


        FlightSearchList searchList = new FlightSearchList();
        searchList.setDepartureDate(date);
        //searchList.setArrivalPort("IST_SAW");
        // searchList.setDeparturePort("DOH");
        searchList.setArrivalPort(arrivalPort);
        searchList.setDeparturePort(departurePort);


        request.setFlightSearchList(new FlightSearchList[]{searchList});
        request.setAdultCount("1");
        request.setCurrency("USD");
        request.setDateOption("1");

        return request;
    }

    @Override
    public void onSelect(SearchObject searchObject) {
        depPort.setSelection(searchObject.getDepPort(), true);
        arPort.setSelection(searchObject.getArPort(), true);
        myDatePickerFragment.setDate(searchObject.getDate());

        search();
    }

    @Override
    public void onSet(String date) {
        dateText.setText(date);
    }


    @Override
    public void responseReceived(JSONObject response, String operation) {
        runOnUiThread(() -> {
            Log.d("response", response.toString());
            if (operation.equals("searchList")) {



                try {
                    JSONArray array = response.getJSONArray("departureRouteList");
                    JSONObject depRouteList = array.getJSONObject(0);
                    JSONArray dailyFlightList = depRouteList.getJSONArray("dailyFlightList");

                    List<JSONArray> flightListArray = new ArrayList<>();
                    for (int i = 0; i < dailyFlightList.length(); i++) {
                        flightListArray.add( dailyFlightList.getJSONObject(0).getJSONArray("flightList"));
                    }

                    AvailabilityResponse availabilityResponse = new AvailabilityResponse();

                    String responseText = "";
                    for (int i = 0; i < dailyFlightList.length(); i++) {
                        Flight flight = new Flight();
                        JSONObject flightJsonObject = dailyFlightList.getJSONObject(i);


                        flight.setDate(flightJsonObject.getString("date"));

                        JSONObject cheapestFare = flightJsonObject.getJSONObject("cheapestFare");

                        flight.setAmount(cheapestFare.getDouble("amount"));
                        flight.setCurrency(cheapestFare.getString("currency"));

                        availabilityResponse.getFlights().add(flight);

                        responseText += "Date: " + flight.getDate() + "\n";
                        responseText += "Currency: " + flight.getCurrency() + "\n";
                        responseText += "Amount: " + flight.getAmount() + "\n";
                        responseText += "\n";
                    }

                    AvailabilityResponse availabilityResponse2 = new AvailabilityResponse();

                    for (JSONArray flightListJsonArray : flightListArray) {
                        for (int i = 0; i < flightListJsonArray.length(); i++) {

                            JSONObject flightJsonObject = flightListJsonArray.getJSONObject(i);
                            Flight flight = gson.fromJson(flightJsonObject.toString(), Flight.class);
                            availabilityResponse2.getFlights().add(flight);

                        }
                    }

                    resultLayout.removeAllViews();

                    this.responseText = new TextView(this);
                    this.responseText.setText(responseText);
                    saveButton.setVisibility(View.VISIBLE);
                    resultLayout.addView(this.responseText);


                    Button goToPnr = new Button(this);
                    goToPnr.setText("Create PNR");
                    goToPnr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    goToPnr.setOnClickListener((view -> {
                        Intent intent = new Intent(this, SelectPnrActivity.class);
                        intent.putExtra("flightList", availabilityResponse2);
                        startActivity(intent);
                    }));

                    resultLayout.addView(goToPnr);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Response failed please check you inputs", Toast.LENGTH_LONG).show();
                }

            } else if (operation.equals("login")) {
                Toast.makeText(getApplicationContext(), "Successfully Loged in", Toast.LENGTH_LONG).show();

            }
        });


    }
}

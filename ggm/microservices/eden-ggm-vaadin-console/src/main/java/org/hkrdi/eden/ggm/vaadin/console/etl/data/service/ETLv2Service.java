package org.hkrdi.eden.ggm.vaadin.console.etl.data.service;

import com.vaadin.flow.spring.annotation.UIScope;

import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.hkrdi.eden.ggm.vaadin.console.etl.data.repository.EtlRepository;
import org.hkrdi.eden.ggm.vaadin.console.etl.v2.ObjectFactory;
import org.hkrdi.eden.ggm.vaadin.console.etl.v2.SDt;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cop.support.BeanUtil;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@UIScope
public class ETLv2Service implements EtlService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ETLv2Service.class);

	public static Map level1Mapping = Arrays.stream(new Integer[][] {
			//unmapped
			{2,4},{32,33},{49,50},{50,51},{5,1},{3,5},{23,24},{22,23},{4,6},{15,14},{1,3},{58,59},{0,2},{41,42},{59,60},{40,41},
			//mapped
			{17,19},{21,17},{19,21},{20,22},{18,20},{7,9},{11,7},{8,10},{10,12},{9,11},{25,27},{26,28},{27,29},{30,26},{28,30},{29,31},{34,36},
			{35,37},{14,13},{51,52},{65,61},{62,64},{63,65},{64,66},{12,16},{54,43},{42,43},{52,54},{53,55},{55,57},{56,58},{57,53},{13,16},{24,25},
			{43,45},{48,44},{45,47},{47,49},{46,48},{33,34},{31,32},{60,62},{61,63},{16,18},{6,8},{44,46},{36,38},{39,35},{38,40},{37,39}
	}).collect(Collectors.toMap(v->v[0], v->v[1]));

	public static Map level2Mapping = Arrays.stream(new Integer[][] {
			{0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}, {5, 1}, {6, 8}, {7, 9}, {8, 10}, {9, 11}, {10, 12}, {11, 7}, {12, 16},
			{13, 17}, {14, 13}, {15, 14}, {16, 15}, {17, 19}, {18, 20}, {19, 21}, {20, 22}, {21, 23}, {22, 18}, {23, 25},
			{24, 26}, {25, 27}, {26, 24}, {27, 29}, {28, 30}, {29, 31}, {30, 32}, {31, 33}, {32, 28}, {33, 34}, {34, 35},
			{35, 36}, {36, 37}, {37, 39}, {38, 40}, {39, 41}, {40, 42}, {41, 43}, {42, 38}, {43, 44}, {44, 45}, {45, 46},
			{46, 47}, {47, 49}, {48, 50}, {49, 51}, {50, 52}, {51, 53}, {52, 48}, {53, 54}, {54, 55}, {55, 56}, {56, 57},
			{57, 59}, {58, 60}, {59, 61}, {60, 62}, {61, 63}, {62, 58}, {63, 64}, {64, 65}, {65, 66}, {66, 68}, {67, 69},
			{68, 70}, {69, 71}, {70, 72}, {71, 67}, {72, 85}, {73, 86}, {74, 76}, {75, 83}, {76, 84}, {77, 88}, {78, 89},
			{79, 90}, {80, 91}, {81, 92}, {82, 87}, {83, 94}, {84, 95}, {85, 93}, {86, 97}, {87, 98}, {88, 99}, {89, 100},
			{90, 101}, {91, 96}, {92, 73}, {93, 74}, {94, 75}, {95, 78}, {96, 79}, {97, 80}, {98, 81}, {99, 82}, {100, 77},
			{101, 208}, {102, 209}, {103, 199}, {104, 206}, {105, 207}, {106, 211}, {107, 212}, {108, 213}, {109, 214},
			{110, 215}, {111, 210}, {112, 217}, {113, 218}, {114, 216}, {115, 220}, {116, 221}, {117, 222}, {118, 223},
			{119, 224}, {120, 219}, {121, 190}, {122, 197}, {123, 198}, {124, 201}, {125, 202}, {126, 203}, {127, 204},
			{128, 205}, {129, 200}, {130, 226}, {131, 227}, {132, 102}, {133, 225}, {134, 229}, {135, 230}, {136, 231},
			{137, 232}, {138, 233}, {139, 228}, {140, 103}, {141, 104}, {142, 105}, {143, 107}, {144, 108}, {145, 109},
			{146, 110}, {147, 111}, {148, 106}, {149, 112}, {150, 113}, {151, 114}, {152, 116}, {153, 117}, {154, 118},
			{155, 119}, {156, 120}, {157, 115}, {158, 162}, {159, 169}, {160, 170}, {161, 171}, {162, 173}, {163, 174},
			{164, 175}, {165, 176}, {166, 177}, {167, 172}, {168, 178}, {169, 179}, {170, 180}, {171, 182}, {172, 183},
			{173, 184}, {174, 185}, {175, 186}, {176, 181}, {177, 187}, {178, 188}, {179, 189}, {180, 192}, {181, 193},
			{182, 194}, {183, 195}, {184, 196}, {185, 191}, {186, 235}, {187, 236}, {188, 237}, {189, 234}, {190, 239},
			{191, 240}, {192, 241}, {193, 242}, {194, 243}, {195, 238}, {196, 244}, {197, 245}, {198, 246}, {199, 248},
			{200, 249}, {201, 250}, {202, 251}, {203, 252}, {204, 247}, {205, 253}, {206, 254}, {207, 255}, {208, 121},
			{209, 257}, {210, 258}, {211, 259}, {212, 260}, {213, 261}, {214, 256}, {215, 122}, {216, 123}, {217, 124},
			{218, 126}, {219, 127}, {220, 128}, {221, 129}, {222, 130}, {223, 125}, {224, 131}, {225, 132}, {226, 133},
			{227, 135}, {228, 136}, {229, 137}, {230, 138}, {231, 139}, {232, 134}, {233, 262}, {234, 263}, {235, 264},
			{236, 265}, {237, 267}, {238, 268}, {239, 269}, {240, 270}, {241, 271}, {242, 266}, {243, 272}, {244, 273},
			{245, 274}, {246, 276}, {247, 277}, {248, 278}, {249, 279}, {250, 280}, {251, 275}, {252, 281}, {253, 282},
			{254, 283}, {255, 140}, {256, 285}, {257, 286}, {258, 287}, {259, 288}, {260, 289}, {261, 284}, {262, 141},
			{263, 142}, {264, 143}, {265, 145}, {266, 146}, {267, 147}, {268, 148}, {269, 149}, {270, 144}, {271, 150},
			{272, 151}, {273, 152}, {274, 154}, {275, 155}, {276, 156}, {277, 157}, {278, 158}, {279, 153}, {280, 290},
			{281, 291}, {282, 292}, {283, 293}, {284, 295}, {285, 296}, {286, 297}, {287, 298}, {288, 299}, {289, 294},
			{290, 300}, {291, 301}, {292, 302}, {293, 304}, {294, 305}, {295, 306}, {296, 307}, {297, 308}, {298, 303},
			{299, 309}, {300, 310}, {301, 311}, {302, 159}, {303, 313}, {304, 314}, {305, 315}, {306, 316}, {307, 317},
			{308, 312}, {309, 160}, {310, 161}, {311, 164}, {312, 165}, {313, 166}, {314, 167}, {315, 168}, {316, 163},
			{317, 318}, {318, 319}, {319, 320}, {320, 321}, {321, 323}, {322, 324}, {323, 325}, {324, 326}, {325, 327},
			{326, 322}, {327, 328}, {328, 329}, {329, 330}, {330, 332}, {331, 333}, {332, 334}, {333, 335}, {334, 336},
			{335, 331}, {336, 337}, {337, 338}, {338, 339}, {339, 341}, {340, 342}, {341, 343}, {342, 344}, {343, 345},
			{344, 340}, {345, 346}, {346, 347}, {347, 348}, {348, 350}, {349, 351}, {350, 352}, {351, 353}, {352, 354},
			{353, 349}
			}).collect(Collectors.toMap(v->v[0], v->v[1]));

	public static Map level3Mapping = Arrays.stream(new Integer[][] {
			{0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}, {5, 1}, {6, 11}, {7, 12}, {8, 7}, {9, 8}, {10, 9}, {11, 10}, {12, 16},
			{13, 17}, {14, 13}, {15, 14}, {16, 15}, {17, 22}, {18, 23}, {19, 18}, {20, 19}, {21, 20}, {22, 21}, {23, 25},
			{24, 26}, {25, 27}, {26, 24}, {27, 32}, {28, 33}, {29, 28}, {30, 29}, {31, 30}, {32, 31}, {33, 34}, {34, 35},
			{35, 36}, {36, 37}, {37, 42}, {38, 43}, {39, 38}, {40, 39}, {41, 40}, {42, 41}, {43, 44}, {44, 45}, {45, 46},
			{46, 47}, {47, 52}, {48, 53}, {49, 48}, {50, 49}, {51, 50}, {52, 51}, {53, 54}, {54, 55}, {55, 56}, {56, 57},
			{57, 62}, {58, 63}, {59, 58}, {60, 59}, {61, 60}, {62, 61}, {63, 64}, {64, 65}, {65, 66}, {66, 71}, {67, 72},
			{68, 67}, {69, 68}, {70, 69}, {71, 70}, {72, 85}, {73, 86}, {74, 76}, {75, 83}, {76, 84}, {77, 91}, {78, 92},
			{79, 87}, {80, 88}, {81, 89}, {82, 90}, {83, 94}, {84, 95}, {85, 93}, {86, 100}, {87, 101}, {88, 96}, {89, 97},
			{90, 98}, {91, 99}, {92, 73}, {93, 74}, {94, 75}, {95, 81}, {96, 82}, {97, 77}, {98, 78}, {99, 79}, {100, 80},
			{102, 209}, {103, 199}, {112, 217}, {113, 218}, {114, 216}, {115, 223}, {116, 224}, {117, 219}, {118, 220},
			{119, 221}, {120, 222}, {121, 190}, {122, 197}, {123, 198}, {124, 204}, {125, 205}, {126, 200}, {127, 201},
			{128, 202}, {129, 203}, {200, 225}, {207, 226}, {208, 227}, {209, 102}, {210, 232}, {211, 233}, {212, 228},
			{213, 229}, {214, 230}, {215, 231}, {216, 180}, {217, 187}, {218, 188}, {219, 189}, {220, 195}, {221, 196},
			{222, 191}, {223, 192}, {224, 193}, {225, 194}, {265, 237}, {266, 103}, {273, 104}, {274, 105}, {275, 110},
			{276, 111}, {277, 106}, {278, 107}, {279, 108}, {280, 109}, {281, 244}, {282, 245}, {283, 246}, {284, 112},
			{285, 251}, {286, 252}, {287, 247}, {288, 248}, {289, 249}, {290, 250}, {291, 113}, {292, 114}, {293, 119},
			{294, 120}, {295, 115}, {296, 116}, {297, 117}, {298, 118}, {299, 162}, {300, 169}, {301, 170}, {302, 171},
			{303, 176}, {304, 177}, {305, 172}, {306, 173}, {307, 174}, {308, 175}, {309, 178}, {310, 179}, {311, 185},
			{312, 186}, {313, 181}, {314, 182}, {315, 183}, {316, 184}, {317, 330}, {318, 337}, {319, 338}, {320, 339},
			{321, 344}, {322, 345}, {323, 340}, {324, 341}, {325, 342}, {326, 343}, {385, 253}, {392, 254}, {393, 255},
			{394, 121}, {395, 260}, {396, 261}, {397, 256}, {398, 257}, {399, 258}, {400, 259}, {401, 122}, {402, 123},
			{403, 124}, {404, 129}, {405, 130}, {406, 125}, {407, 126}, {408, 127}, {409, 128}, {410, 131}, {411, 132},
			{412, 133}, {413, 138}, {414, 139}, {415, 134}, {416, 135}, {417, 136}, {418, 137}, {431, 265}, {438, 272},
			{439, 273}, {440, 274}, {441, 279}, {442, 280}, {443, 275}, {444, 276}, {445, 277}, {446, 278}, {447, 281},
			{448, 282}, {449, 283}, {450, 140}, {451, 288}, {452, 289}, {453, 284}, {454, 285}, {455, 286}, {456, 287},
			{457, 141}, {458, 54}, {459, 143}, {460, 148}, {461, 149}, {462, 144}, {463, 145}, {464, 146}, {465, 147},
			{466, 150}, {467, 151}, {468, 152}, {469, 157}, {470, 158}, {471, 153}, {472, 154}, {473, 155}, {474, 156},
			{475, 159}, {476, 160}, {477, 161}, {478, 167}, {479, 168}, {480, 163}, {481, 164}, {482, 165}, {483, 166},
			{487, 293}, {494, 300}, {495, 301}, {496, 302}, {497, 307}, {498, 308}, {499, 303}, {500, 304}, {501, 305},
			{502, 306}, {503, 309}, {504, 310}, {505, 311}, {506, 316}, {507, 317}, {508, 312}, {509, 313}, {510, 314},
			{511, 315}, {515, 321}, {522, 328}, {523, 329}, {524, 335}, {525, 336}, {526, 331}, {527, 332}, {528, 333}, {529, 334}

	}).collect(Collectors.toMap(v->v[0], v->v[1]));

	public static Map level4Mapping = Arrays.stream(new Integer[][] {
		{0,2},{1,3},{2,4},{3,5},{4,6},{5,1},{6,11},{7,12},{8,7},{9,8},{10,9},{11,10},{12,15},{13,16},{14,13},{15,14},{16,21},{17,22},{18,17},{19,18},{20,19},{21,20},{22,23},
		{23,24},{24,25},{25,30},{26,31},{27,26},{28,27},{29,28},{30,29},{31,32},{32,33},{33,34},{34,39},{35,40},{36,35},{37,36},{38,37},{39,38},{40,41},{41,42},{42,43},{43,48},
		{44,49},{45,44},{46,45},{47,46},{48,47},{49,50},{50,51},{51,52},{52,57},{53,58},{54,53},{55,54},{56,55},{57,56},{58,59},{59,60},{60,65},{61,66},{62,61},{63,62},{64,63},
		{65,64},{66,77},{67,78},{68,69},{69,76},{70,83},{71,84},{72,79},{73,80},{74,81},{75,82},{76,85},{77,86},{78,91},{79,92},{80,87},{81,88},{82,89},{83,90},{84,67},{85,68},
		{86,74},{87,75},{88,70},{89,71},{90,72},{91,73},{92,187},{93,188},{94,179},{95,186},{96,193},{97,194},{98,189},{99,190},{100,191},{101,192},{102,195},{103,196},{104,201},
		{105,202},{106,197},{107,198},{108,199},{109,200},{110,171},{111,178},{112,184},{113,185},{114,180},{115,181},{116,182},{117,183},{180,203},{187,204},{188,93},{189,209},
		{190,210},{191,205},{192,206},{193,207},{194,208},{195,162},{196,169},{197,170},{198,176},{199,177},{200,172},{201,173},{202,174},{203,175},{230,211},{237,212},{238,213},
		{239,94},{240,218},{241,219},{242,214},{243,215},{244,216},{245,217},{246,95},{247,100},{248,101},{249,96},{250,97},{251,98},{252,99},{253,220},{254,221},{255,102},{256,226},
		{257,227},{258,222},{259,223},{260,224},{261,225},{262,103},{263,108},{264,109},{265,104},{266,105},{267,106},{268,107},{269,146},{270,153},{271,154},{272,159},{273,160},
		{274,155},{275,156},{276,157},{277,158},{278,161},{279,167},{280,168},{281,163},{282,164},{283,165},{284,166},{285,296},{286,303},{287,304},{288,309},{289,310},{290,305},
		{291,306},{292,307},{293,308},{294,311},{295,312},{296,317},{297,318},{298,313},{299,314},{300,315},{301,316},{345,228},{352,229},{353,110},{354,234},{355,235},{356,230},
		{357,231},{358,232},{359,233},{360,111},{361,112},{362,117},{363,118},{364,113},{365,114},{366,115},{367,116},{368,119},{369,120},{370,125},{371,126},{372,121},{373,122},
		{374,123},{375,124},{378,236},{385,237},{386,111},{387,243},{388,244},{389,239},{390,240},{391,241},{392,242},{393,238},{394,246},{395,251},{396,252},{397,247},{398,248},
		{399,249},{400,250},{401,253},{402,254},{403,127},{404,259},{405,260},{406,255},{407,256},{408,257},{409,258},{410,263},{411,129},{412,134},{413,135},{414,130},{415,131},
		{416,132},{417,133},{418,136},{419,137},{420,142},{421,143},{422,138},{423,139},{424,140},{425,141},{426,144},{427,145},{428,151},{429,152},{430,147},{431,148},{432,149},
		{433,150},{434,261},{435,262},{436,263},{437,268},{438,269},{439,264},{440,265},{441,266},{442,267},{443,270},{444,271},{445,276},{446,277},{447,272},{448,273},{449,274},
		{450,275},{451,278},{452,279},{453,284},{454,285},{455,280},{456,281},{457,282},{458,283},{459,286},{460,287},{461,288},{462,293},{463,294},{464,289},{465,290},{466,291},
		{467,292},{468,295},{469,301},{470,302},{471,297},{472,298},{473,299},{474,300}
	}).collect(Collectors.toMap(v->v[0], v->v[1]));

	public static Map level5Mapping = Arrays.stream(new Integer[][] {
		{0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6}, {5, 1}, {6, 8}, {7, 9}, {8, 10}, {9, 11}, {10, 12}, {11, 7}, {12, 15}, {13, 16}, {14, 13}, {15, 14}, {16, 18}, {17, 19}, {18, 20},
		{19, 21}, {20, 22}, {21, 17}, {22, 23}, {23, 24}, {24, 25}, {25, 27}, {26, 28}, {27, 29}, {28, 30}, {29, 31}, {30, 26}, {31, 32}, {32, 33}, {33, 34}, {34, 36}, {35, 37},
		{36, 38}, {37, 39}, {38, 40}, {39, 35}, {40, 41}, {41, 42}, {42, 43}, {43, 45}, {44, 46}, {45, 47}, {46, 48}, {47, 49}, {48, 44}, {49, 50}, {50, 51}, {51, 52}, {52, 54},
		{53, 55}, {54, 56}, {55, 57}, {56, 58}, {57, 53}, {58, 59}, {59, 60}, {60, 62}, {61, 63}, {62, 64}, {63, 65}, {64, 66}, {65, 61}, {66, 77}, {67, 78}, {68, 69}, {69, 76},
		{70, 80}, {71, 81}, {72, 82}, {73, 83}, {74, 84}, {75, 79}, {76, 85}, {77, 86}, {78, 88}, {79, 89}, {80, 90}, {81, 91}, {82, 92}, {83, 87}, {84, 67}, {85, 68}, {86, 71},
		{87, 72}, {88, 73}, {89, 74}, {90, 75}, {91, 70}, {92, 187}, {93, 188}, {94, 179}, {95, 186}, {96, 190}, {97, 191}, {98, 192}, {99, 193}, {100, 194}, {101, 189}, {102, 195},
		{103, 196}, {104, 198}, {105, 199}, {106, 200}, {107, 201}, {108, 202}, {109, 197}, {110, 171}, {111, 178}, {112, 181}, {113, 182}, {114, 183}, {115, 184}, {116, 185}, {117, 180},
		{118, 345}, {119, 346}, {120, 337}, {121, 344}, {122, 348}, {123, 349}, {124, 350}, {125, 351}, {126, 352}, {127, 347}, {128, 353}, {129, 354}, {130, 356}, {131, 357}, {132, 358},
		{133, 359}, {134, 360}, {135, 355}, {136, 329}, {137, 336}, {138, 339}, {139, 340}, {140, 341}, {141, 342}, {142, 343}, {143, 338}, {144, 361}, {145, 362}, {146, 203}, {147, 364},
		{148, 365}, {149, 366}, {150, 367}, {151, 368}, {152, 363}, {153, 204}, {154, 93}, {155, 206}, {156, 207}, {157, 208}, {158, 209}, {159, 210}, {160, 205}, {161, 94}, {162, 95},
		{163, 97}, {164, 98}, {165, 99}, {166, 100}, {167, 101}, {168, 96}, {169, 154}, {170, 161}, {171, 162}, {172, 164}, {173, 165}, {174, 166}, {175, 167}, {176, 168}, {177, 163},
		{178, 169}, {179, 170}, {180, 173}, {181, 174}, {182, 175}, {183, 176}, {184, 177}, {185, 172}, {186, 321}, {187, 328}, {188, 331}, {189, 332}, {190, 333}, {191, 334}, {192, 335},
		{193, 330}, {194, 369}, {195, 370}, {196, 211}, {197, 372}, {198, 373}, {199, 374}, {200, 375}, {201, 376}, {202, 371}, {203, 212}, {204, 213}, {205, 215}, {206, 216}, {207, 217},
		{208, 218}, {209, 219}, {210, 214}, {211, 220}, {212, 221}, {213, 102}, {214, 223}, {215, 224}, {216, 225}, {217, 226}, {218, 227}, {219, 222}, {220, 103}, {221, 105}, {222, 106},
		{223, 107}, {224, 108}, {225, 109}, {226, 104}, {227, 228}, {228, 229}, {229, 110}, {230, 231}, {231, 232}, {232, 233}, {233, 234}, {234, 235}, {235, 230}, {236, 111}, {237, 112},
		{238, 114}, {239, 115}, {240, 116}, {241, 117}, {242, 118}, {243, 113}, {244, 119}, {245, 120}, {246, 122}, {247, 123}, {248, 124}, {249, 125}, {250, 126}, {251, 121}, {252, 127},
		{253, 128}, {254, 129}, {255, 131}, {256, 132}, {257, 133}, {258, 134}, {259, 135}, {260, 130}, {261, 136}, {262, 137}, {263, 139}, {264, 140}, {265, 141}, {266, 142}, {267, 143},
		{268, 138}, {269, 144}, {270, 145}, {271, 146}, {272, 148}, {273, 149}, {274, 150}, {275, 151}, {276, 152}, {277, 147}, {278, 153}, {279, 156}, {280, 157}, {281, 158}, {282, 159},
		{283, 160}, {284, 155}, {285, 288}, {286, 295}, {287, 296}, {288, 298}, {289, 299}, {290, 300}, {291, 301}, {292, 302}, {293, 297}, {294, 303}, {295, 304}, {296, 306}, {297, 307},
		{298, 308}, {299, 309}, {300, 310}, {301, 305}, {302, 311}, {303, 312}, {304, 314}, {305, 315}, {306, 316}, {307, 317}, {308, 318}, {309, 313}, {310, 319}, {311, 320}, {312, 323},
		{313, 324}, {314, 325}, {315, 326}, {316, 327}, {317, 322}, {318, 377}, {319, 378}, {320, 379}, {321, 381}, {322, 382}, {323, 383}, {324, 384}, {325, 385}, {326, 380}, {327, 386},
		{328, 387}, {329, 389}, {330, 390}, {331, 391}, {332, 392}, {333, 393}, {334, 388}, {335, 394}, {336, 395}, {337, 397}, {338, 398}, {339, 399}, {340, 400}, {341, 401}, {342, 396},
		{343, 402}, {344, 403}, {345, 236}, {346, 405}, {347, 406}, {348, 407}, {349, 408}, {350, 409}, {351, 404}, {352, 237}, {353, 238}, {354, 240}, {355, 241}, {356, 242}, {357, 243},
		{358, 244}, {359, 239}, {360, 245}, {361, 246}, {362, 248}, {363, 249}, {364, 250}, {365, 251}, {366, 252}, {367, 247}, {368, 253}, {369, 254}, {370, 256}, {371, 257}, {372, 258},
		{373, 259}, {374, 260}, {375, 255}, {376, 410}, {377, 411}, {378, 412}, {379, 414}, {380, 415}, {381, 416}, {382, 417}, {383, 418}, {384, 413}, {385, 419}, {386, 420}, {387, 422},
		{388, 423}, {389, 424}, {390, 425}, {391, 426}, {392, 421}, {393, 427}, {394, 428}, {395, 430}, {396, 431}, {397, 432}, {398, 433}, {399, 434}, {400, 429}, {401, 435}, {402, 436},
		{403, 261}, {404, 438}, {405, 439}, {406, 440}, {407, 441}, {408, 442}, {409, 437}, {410, 262}, {411, 263}, {412, 265}, {413, 266}, {414, 267}, {415, 268}, {416, 269}, {417, 264},
		{418, 270}, {419, 271}, {420, 273}, {421, 274}, {422, 275}, {423, 276}, {424, 277}, {425, 272}, {426, 278}, {427, 279}, {428, 281}, {429, 282}, {430, 283}, {431, 284}, {432, 285},
		{433, 280}, {461, 286}, {468, 287}, {469, 290}, {470, 291}, {471, 292}, {472, 293}, {473, 294}, {474, 289}, {517, 552}, {526, 559}, {527, 560}, {528, 562}, {529, 563}, {530, 564},
		{531, 565}, {532, 566}, {533, 561}, {542, 567}, {543, 568}, {544, 570}, {545, 571}, {546, 572}, {547, 573}, {548, 574}, {549, 569}, {558, 575}, {559, 576}, {560, 578}, {561, 579},
		{562, 580}, {563, 581}, {564, 582}, {565, 577}, {574, 583}, {575, 584}, {576, 586}, {577, 587}, {578, 588}, {579, 589}, {580, 590}, {581, 585}, {590, 591}, {591, 592}, {592, 593},
		{593, 595}, {594, 596}, {595, 597}, {596, 598}, {597, 599}, {598, 594}, {599, 600}, {600, 601}, {601, 603}, {602, 604}, {603, 605}, {604, 606}, {605, 607}, {606, 602}, {607, 608},
		{608, 609}, {609, 611}, {610, 612}, {611, 613}, {612, 614}, {613, 615}, {614, 610}, {615, 616}, {616, 617}, {617, 619}, {618, 620}, {619, 621}, {620, 622}, {621, 623}, {622, 618},
		{623, 624}, {624, 625}, {625, 627}, {626, 628}, {627, 629}, {628, 630}, {629, 631}, {630, 626}
	}).collect(Collectors.toMap(v->v[0], v->v[1]));

	private SDt unmarshall(InputStream is) throws JAXBException, IOException {
		return (SDt) JAXBContext.newInstance(SDt.class, ObjectFactory.class).createUnmarshaller().unmarshal(
				is);
	}

	public Integer mapping(Integer level, Integer originalNode) {
		Integer node = null;
		if (level ==1 ) {
			node = (Integer)level1Mapping.get(originalNode);
		}else if (level ==2 ) {
			node = (Integer)level2Mapping.get(originalNode);
		}else if (level ==3 ) {
			node = (Integer)level3Mapping.get(originalNode);
		}else if (level ==4 ) {
			node = (Integer)level4Mapping.get(originalNode);
		}else if (level ==5 ) {
			node = (Integer)level5Mapping.get(originalNode);
		}
		if (node != null) {
			return node;
		}
		return null;
	}

	@Override
	public void etl(EtlRepository etlRepository, ApplicationDataRepositoryService applicationDataService,
					ApplicationRepositoryService applicationService, Application application, InputStream is)
			throws IOException, JAXBException {
		SDt sdt = unmarshall(is);
		etlLevel1(etlRepository,sdt, applicationDataService, applicationService, application);
		etlLevel2(etlRepository,sdt, applicationDataService, applicationService, application);
		etlLevel3(etlRepository,sdt, applicationDataService, applicationService, application);
		etlLevel4(etlRepository,sdt, applicationDataService, applicationService, application);
		etlLevel5(etlRepository,sdt, applicationDataService, applicationService, application);
	}

	public void etlLevel1(EtlRepository etlRepository, SDt sdt, ApplicationDataRepositoryService applicationDataService,
						  ApplicationRepositoryService applicationService, Application application) throws FileNotFoundException, JAXBException{
		LOGGER.info("Import for level 1 started:" + application.getBrief().getBrief());
		Set<Integer> persisted = new HashSet<>();
		sdt.getT1().getNodeDt().getSNodeDt().stream().forEach(c->{
			if (c.getNodeNr()!=null && !persisted.contains((int)c.getNodeNr())) {
				persistEtl(1, etlRepository, applicationDataService, applicationService, application, c.getNodeNr()!=null?(int)c.getNodeNr():null, c.getNodeTtl(), c.getNodeCnt());
				if (!c.getNodeCnt().isEmpty()) {persisted.add((int)c.getNodeNr());}
			}
		});
		LOGGER.info("Import for level 1 completed:" + application.getBrief().getBrief());
	}

	public void etlLevel2(EtlRepository etlRepository, SDt sdt, ApplicationDataRepositoryService applicationDataService,
						  ApplicationRepositoryService applicationService, Application application) throws FileNotFoundException, JAXBException{
		LOGGER.info("Import for level 2 started:" + application.getBrief().getBrief());
		Set<Integer> persisted = new HashSet<>();
		sdt.getT2().getNodeDt().getSNodeDt().stream().forEach(c->{
			if (c.getNodeNr()!=null && !persisted.contains((int)c.getNodeNr())) {
				persistEtl(2, etlRepository, applicationDataService, applicationService, application, c.getNodeNr()!=null?(int)c.getNodeNr():null, c.getNodeTtl(), c.getNodeCnt());
				if (!c.getNodeCnt().isEmpty()) {persisted.add((int)c.getNodeNr());}
			}
		});
		LOGGER.info("Import for level 2 completed:" + application.getBrief().getBrief());
	}

	public void etlLevel3(EtlRepository etlRepository, SDt sdt, ApplicationDataRepositoryService applicationDataService,
						  ApplicationRepositoryService applicationService, Application application) throws FileNotFoundException, JAXBException{
		LOGGER.info("Import for level 3 started:" + application.getBrief().getBrief());
		Set<Integer> persisted = new HashSet<>();
		sdt.getT3().getNodeDt().getSNodeDt().stream().forEach(c->{
			if (c.getNodeNr()!=null && !persisted.contains((int)c.getNodeNr())) {
				persistEtl(3, etlRepository, applicationDataService, applicationService, application, c.getNodeNr()!=null?(int)c.getNodeNr():null, c.getNodeTtl(), c.getNodeCnt());
				if (!c.getNodeCnt().isEmpty()) {persisted.add((int)c.getNodeNr());}
			}
		});
		LOGGER.info("Import for level 3 completed:" + application.getBrief().getBrief());
	}

	public void etlLevel4(EtlRepository etlRepository, SDt sdt, ApplicationDataRepositoryService applicationDataService,
						  ApplicationRepositoryService applicationService, Application application) throws FileNotFoundException, JAXBException{
		LOGGER.info("Import for level 4 started:" + application.getBrief().getBrief());
		Set<Integer> persisted = new HashSet<>();
		sdt.getT4().getNodeDt().getSNodeDt().stream().forEach(c->{
			if (c.getNodeNr()!=null && !persisted.contains((int)c.getNodeNr())) {
				persistEtl(4, etlRepository, applicationDataService, applicationService, application, c.getNodeNr()!=null?(int)c.getNodeNr():null, c.getNodeTtl(), c.getNodeCnt());
				if (!c.getNodeCnt().isEmpty()) {persisted.add((int)c.getNodeNr());}
			}
		});
		LOGGER.info("Import for level 4 completed:" + application.getBrief().getBrief());
	}

	public void etlLevel5(EtlRepository etlRepository, SDt sdt, ApplicationDataRepositoryService applicationDataService,
						  ApplicationRepositoryService applicationService, Application application) throws FileNotFoundException, JAXBException{
		LOGGER.info("Import for level 5 started:" + application.getBrief().getBrief());
		Set<Integer> persisted = new HashSet<>();
		sdt.getT5().getNodeDt().getSNodeDt().stream().forEach(c->{
			if (c.getNodeNr()!=null && !persisted.contains((int)c.getNodeNr())) {
				persistEtl(5, etlRepository, applicationDataService, applicationService, application, c.getNodeNr()!=null?(int)c.getNodeNr():null, c.getNodeTtl(), c.getNodeCnt());
				if (!c.getNodeCnt().isEmpty()) {persisted.add((int)c.getNodeNr());}
			}
		});
		LOGGER.info("Import for level 5 completed:" + application.getBrief().getBrief());
	}

	public String getNetworkName(int level) {
		if (level == 1) return "SUSTAINABLE_EDGES::1";
		else if (level == 2) return "SUSTAINABLE_VERTICES::1";
		else if (level == 3) return "METABOLIC_VERTICES::1";
		else if (level == 4) return "METABOLIC_EDGES::1";
		else if (level == 5) return "SUSTAINABLE_EDGES::2";
		return "SUSTAINABLE_VERTICES::1";
	}

	private void persistEtl(Integer level, EtlRepository etlRepository, ApplicationDataRepositoryService applicationDataService,
							ApplicationRepositoryService applicationService, Application application, Integer nodeNr, String nodeTtl, String nodeCnt) {
		if ((nodeTtl == null || nodeTtl.isEmpty()) &&
				(nodeCnt == null || nodeCnt.isEmpty())) {
			return;
		}
		String log = null;
		Integer mappedToNode = null;
		if (mapping(level, nodeNr)!=null) {
			try {
				persistSemantics(applicationDataService, perpareSemantic(application, level, nodeNr, nodeTtl, nodeCnt));
				mappedToNode = mapping(level, nodeNr);
			}catch(Exception e) {
				log = "Failed: "+e.getMessage();
			}
		}
		persistEtl(etlRepository, new Etl(application, level, mappedToNode, nodeTtl, nodeTtl, nodeCnt, log, nodeNr, mappedToNode != null), applicationService);
		}

	@Override
	public void persistSemantics(ApplicationDataRepositoryService applicationDataService, Etl etl) {
		DataMap dataMap = BeanUtil.getBean(CoherentSpaceService.class).getNetworkDataMaps(getNetworkName(etl.getLevel()))
				.stream().filter(DataMapFilterUtil.byAddressIndex(new Long(etl.getNode()))).findFirst().orElse(null);
		if(dataMap == null) {
			throw new IllegalArgumentException("Node index nod found in network");
		}

		ApplicationData applicationData = applicationDataService.getApplicationData(etl.getApplication().getId(), dataMap);
		String newSemantic = etl.getDescription() +
				(applicationData.getSemantic().isEmpty()?"":"\n" + applicationData.getSemantic());
		applicationData.setSemantic(newSemantic);
		applicationData.setSyntax(Optional.ofNullable(applicationData.getSyntax()).orElse(""));
		applicationData.setWhoWhat(Optional.ofNullable(applicationData.getWhoWhat()).orElse(""));
		applicationData.setHow(Optional.ofNullable(applicationData.getHow()).orElse(""));
		applicationData.setWhy(Optional.ofNullable(applicationData.getWhy()).orElse(""));
		applicationData.setWhereWhen(Optional.ofNullable(applicationData.getWhereWhen()).orElse(""));
		applicationDataService.save(applicationData, true);
	}

	private Etl perpareSemantic(Application application, int level, int node, String nodeTtl, String nodeCnt) {
		String networkName = getNetworkName(level);
		networkName = networkName.substring(0,1)+networkName.substring(networkName.indexOf("_")+1, networkName.indexOf("_")+2) + networkName.substring(networkName.indexOf("::")+2);

		String description = nodeTtl.isEmpty()?nodeCnt:(nodeTtl.equals(nodeCnt)?nodeCnt:(nodeTtl+"\n"+nodeCnt));
		description = networkName + "\n" + description;

		return new Etl(application, level, mapping(level, node),
				nodeTtl, nodeTtl, description);
	}

}
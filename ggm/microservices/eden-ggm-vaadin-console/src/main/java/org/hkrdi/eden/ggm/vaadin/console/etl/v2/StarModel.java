package org.hkrdi.eden.ggm.vaadin.console.etl.v2;

import com.vaadin.flow.theme.material.Material;
import org.w3c.dom.css.Rect;

import java.util.*;

public class StarModel extends MonoBehaviour{
	
	public StarModel() {
		super();
	}
	
	public class SaveableData{}
	
				
    public enum FRACTALIZATION_TYPE { BASED_ON_MARGINS, BASED_ON_NODES, SPECIAL, TYPE_4, TYPE_5 };

    public enum NEIGHBOR_POSITION { N_E, E, S_E, S_V, V, N_V,            //margins
                                    N, N_E_E, S_E_E, S, S_V_V, N_V_V};   //nodes

    //public FRACTALIZATION_TYPE FractalizationType = FRACTALIZATION_TYPE.BASED_ON_MARGINS;

    public StarModel N_E;
    public StarModel E;
    public StarModel S_E; 
    public StarModel S_V; 
    public StarModel V;
    public StarModel N_V;

    public StarModel N;
    public StarModel N_E_E;
    public StarModel S_E_E;
    public StarModel S;
    public StarModel S_V_V;
    public StarModel N_V_V; 

    public GameObject OriginalStarObject;

    public Transform N_E_Transform = new Transform();
    public Transform E_Transorm = new Transform();
    public Transform S_E_Transform = new Transform();
    public Transform S_V_Transform = new Transform();
    public Transform V_Transform = new Transform();
    public Transform N_V_Transform = new Transform();

    public Transform N_Transform_n = new Transform();
    public Transform N_E_E_Transform_n = new Transform();
    public Transform S_E_E_Transform_n = new Transform();
    public Transform S_Transform_n = new Transform();
    public Transform S_V_V_Transform_n = new Transform();
    public Transform N_V_V_Transform_n = new Transform();

    public boolean StarEnabled = false;

    public GameObject Star = new GameObject();
    public GameObject Sphere = new GameObject();

    public Transform N_E_MarginTransform = new Transform();
    public GameObject N_E_MarginPrefab = new GameObject();;
    public GameObject N_E_MARGIN = new GameObject();

    public Transform E_MarginTransform = new Transform();
    public GameObject E_MarginPrefab = new GameObject();
    public GameObject E_MARGIN = new GameObject();

    public Transform S_E_MarginTransform = new Transform();
    public GameObject S_E_MarginPrefab = new GameObject();
    public GameObject S_E_MARGIN = new GameObject();

    public Transform S_V_MarginTransform = new Transform();
    public GameObject S_V_MarginPrefab = new GameObject();
    public GameObject S_V_MARGIN = new GameObject();

    public Transform V_MarginTransform = new Transform();
    public GameObject V_MarginPrefab = new GameObject();
    public GameObject V_MARGIN = new GameObject();

    public Transform N_V_MarginTransform = new Transform();
    public GameObject N_V_MarginPrefab = new GameObject();
    public GameObject N_V_MARGIN = new GameObject();

    public Transform Node_2OCLOCK_Transform = new Transform();
    public GameObject Node_2OCLOCK;
    public Transform Node_4OCLOCK_Transform = new Transform();
    public GameObject Node_4OCLOCK;
    public Transform Node_6OCLOCK_Transform = new Transform();
    public GameObject Node_6OCLOCK;
    public Transform Node_8OCLOCK_Transform = new Transform();
    public GameObject Node_8OCLOCK;
    public Transform Node_10OCLOCK_Transform = new Transform();
    public GameObject Node_10OCLOCK;
    public Transform Node_12OCLOCK_Transform = new Transform();
    public GameObject Node_12OCLOCK;

    public GameObject NodePrefab = new GameObject();

    public GameObject[] CenterNodes = new GameObject[] {new GameObject(),new GameObject(),new GameObject(),new GameObject(),new GameObject(),new GameObject()};
    public GameObject[] CenterMargins = new GameObject[18];;

    public Transform Parent = new Transform();

    private GameObject ThisGO;
    private static int count = 0;

    private static int incrementer = 0;
    private int m_localNumber = 0;
    private static int s_starCount = 0;
    private static int s_nodeIndex = 0;
    private Vector m_screenPos;

    private static StarModel m_rootModel;
    private static List<StarModel> s_allStars_TYPE1_MARGINS = new ArrayList<StarModel>();
    private static List<StarModel> s_allStars_TYPE2_NODES = new ArrayList<StarModel>();
    private static List<StarModel> s_allStars_TYPE3_SPECIAL = new ArrayList<StarModel>();
    private static List<StarModel> s_allStars_TYPE4 = new ArrayList<StarModel>();
    private static List<StarModel> s_allStars_TYPE5 = new ArrayList<StarModel>();

    private static List<GameObject> s_allNodes_TYPE1_MARGINS = new ArrayList<GameObject>();
    private static List<GameObject> s_allNodes_TYPE2_NODES = new ArrayList<GameObject>();
    private static List<GameObject> s_allNodes_TYPE3_SPECIAL = new ArrayList<GameObject>();
    private static List<GameObject> s_allNodes_TYPE4 = new ArrayList<GameObject>();
    private static List<GameObject> s_allNodes_TYPE5 = new ArrayList<GameObject>();

    private static List<Integer> s_includedNodes_TYPE1_MARGINS = new ArrayList<Integer>();
    private static List<Integer> s_includedNodes_TYPE2_NODES = new ArrayList<Integer>();
    private static List<Integer> s_includedNodes_TYPE3_SPECIAL = new ArrayList<Integer>();
    private static List<Integer> s_includedNodes_TYPE4 = new ArrayList<Integer>();
    private static List<Integer> s_includedNodes_TYPE5 = new ArrayList<Integer>();

    private static Map<Integer, List<GameObject>> s_nodesDictionary_TYPE1_MARGINS = new HashMap<Integer, List<GameObject>>();
    private static Map<Integer, List<GameObject>> s_nodesDictionary_TYPE2_NODES   = new HashMap<Integer, List<GameObject>>();
    private static Map<Integer, List<GameObject>> s_nodesDictionary_TYPE3_SPECIAL = new HashMap<Integer, List<GameObject>>();
    private static Map<Integer, List<GameObject>> s_nodesDictionary_TYPE4 = new HashMap<Integer, List<GameObject>>();
    private static Map<Integer, List<GameObject>> s_nodesDictionary_TYPE5 = new HashMap<Integer, List<GameObject>>();

    private static float s_type1_alpha = 1f;
    private static float s_type2_alpha = 1f;
    private static float s_type3_alpha = 1f;
    private static float s_type4_alpha = 1f;
    private static float s_type5_alpha = 1f;

    public static FRACTALIZATION_TYPE CurrentFType;

    public static SaveableData s_SaveableData = new StarModel().new SaveableData();

    public static Map<Integer, List<GameObject>> GetNodeDictionary(FRACTALIZATION_TYPE fType)
    {
        switch(fType)
        {
            case BASED_ON_MARGINS:
                return s_nodesDictionary_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return s_nodesDictionary_TYPE2_NODES;
            case SPECIAL:
                return s_nodesDictionary_TYPE3_SPECIAL;
            case TYPE_4:
                return s_nodesDictionary_TYPE4;
            case TYPE_5:
                return s_nodesDictionary_TYPE5;
        }

        return new HashMap<Integer, List<GameObject>>();
    }

    public static void InitializeNodeDictionaries()
    {
        
        for(GameObject g:GetAllNodes(FRACTALIZATION_TYPE.BASED_ON_MARGINS))
        {
            if (g.GetComponent().GetNodeNumber() != -1)
            {
                List<GameObject> lTemp;
                if ((lTemp = GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_MARGINS).get(g.GetComponent().GetNodeNumber()))==null)
                {
                    lTemp = new ArrayList<GameObject>();
                }

                GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_MARGINS).remove(g.GetComponent().GetNodeNumber());
                lTemp.add(g);
                GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_MARGINS).put(g.GetComponent().GetNodeNumber(), lTemp);
            }
        }

        for(GameObject g:GetAllNodes(FRACTALIZATION_TYPE.BASED_ON_NODES))
        {
            if (g.GetComponent().GetNodeNumber() != -1)
            {
                List<GameObject> lTemp;
                if ((lTemp = GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_NODES).get(g.GetComponent().GetNodeNumber()))==null)
                {
                    lTemp = new ArrayList<GameObject>();
                }

                GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_NODES).remove(g.GetComponent().GetNodeNumber());
                lTemp.add(g);
                GetNodeDictionary(FRACTALIZATION_TYPE.BASED_ON_NODES).put(g.GetComponent().GetNodeNumber(), lTemp);
            }
        }

        for(GameObject g:GetAllNodes(FRACTALIZATION_TYPE.SPECIAL))
        {
            if (g.GetComponent().GetNodeNumber() != -1)
            {
                List<GameObject> lTemp;
                if ((lTemp = GetNodeDictionary(FRACTALIZATION_TYPE.SPECIAL).get(g.GetComponent().GetNodeNumber()))==null)
                {
                    lTemp = new ArrayList<GameObject>();
                }

                GetNodeDictionary(FRACTALIZATION_TYPE.SPECIAL).remove(g.GetComponent().GetNodeNumber());
                lTemp.add(g);
                GetNodeDictionary(FRACTALIZATION_TYPE.SPECIAL).put(g.GetComponent().GetNodeNumber(), lTemp);
            }
        }

        for(GameObject g:GetAllNodes(FRACTALIZATION_TYPE.TYPE_4))
        {
            if (g.GetComponent().GetNodeNumber() != -1)
            {
                List<GameObject> lTemp;
            	if ((lTemp = GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_4).get(g.GetComponent().GetNodeNumber()))==null)
                {
                    lTemp = new ArrayList<GameObject>();
                }

                GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_4).remove(g.GetComponent().GetNodeNumber());
                lTemp.add(g);
                GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_4).put(g.GetComponent().GetNodeNumber(), lTemp);
            }
        }

        for(GameObject g:GetAllNodes(FRACTALIZATION_TYPE.TYPE_5))
        {
            if (g.GetComponent().GetNodeNumber() != -1)
            {
                List<GameObject> lTemp;
                if ((lTemp = GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_5).get(g.GetComponent().GetNodeNumber()))==null)
                {
                    lTemp = new ArrayList<GameObject>();
                }

                GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_5).remove(g.GetComponent().GetNodeNumber());
                lTemp.add(g);
                GetNodeDictionary(FRACTALIZATION_TYPE.TYPE_5).put(g.GetComponent().GetNodeNumber(), lTemp);
            }
        }
    }

    public static List<Integer> GetIncludedNodes(FRACTALIZATION_TYPE fType)
    {
        switch (fType)
        {
            case BASED_ON_MARGINS:
                return s_includedNodes_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return s_includedNodes_TYPE2_NODES;
            case SPECIAL:
                return s_includedNodes_TYPE3_SPECIAL;
            case TYPE_4:
                return s_includedNodes_TYPE4;
            case TYPE_5:
                return s_includedNodes_TYPE5;
        }

        return new ArrayList<Integer>();
    }

    public static List<StarModel> GetAllStars(FRACTALIZATION_TYPE fType)
    {
        switch(fType)
        {
            case BASED_ON_MARGINS:
                return s_allStars_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return s_allStars_TYPE2_NODES;
            case SPECIAL:
                return s_allStars_TYPE3_SPECIAL;
            case TYPE_4:
                return s_allStars_TYPE4;
            case TYPE_5:
                return s_allStars_TYPE5;
        }

        return new ArrayList<StarModel>();
    }

    public static void SetStructureAlphaValue(FRACTALIZATION_TYPE fType, float value)
    {
        switch(fType)
        {
            case BASED_ON_MARGINS:
                s_type1_alpha = value;
                break;
            case BASED_ON_NODES:
                s_type2_alpha = value;
                break;
            case SPECIAL:
                s_type3_alpha = value;
                break;
            case TYPE_4:
                s_type4_alpha = value;
                break;
            case TYPE_5:
                s_type5_alpha = value;
                break;
        }
    }

    public static float GetStructureAlphaValue(FRACTALIZATION_TYPE fType)
    {
        switch (fType)
        {
            case BASED_ON_MARGINS:
                return s_type1_alpha;
            case BASED_ON_NODES:
                return s_type2_alpha;
            case SPECIAL:
                return s_type3_alpha;
            case TYPE_4:
                return s_type4_alpha;
            case TYPE_5:
                return s_type5_alpha;
        }

        return 1f;
    }

    public static List<GameObject> GetAllNodes(FRACTALIZATION_TYPE fType)
    {
        switch (fType)
        {
            case BASED_ON_MARGINS:
                return s_allNodes_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return s_allNodes_TYPE2_NODES;
            case SPECIAL:
                return s_allNodes_TYPE3_SPECIAL;
            case TYPE_4:
                return s_allNodes_TYPE4;
            case TYPE_5:
                return s_allNodes_TYPE5;

        }

        return new ArrayList<GameObject>();
    }

    public class ConnectionModel{

    	public Map Connections_FromStart;
    };
    
    public static List<LinkModel> AllLinks_TYPE1_MARGINS = new ArrayList<LinkModel>();
    public static List<LinkModel> AllLinks_TYPE2_NODES = new ArrayList<LinkModel>();
    public static List<LinkModel> AllLinks_TYPE3_SPECIAL = new ArrayList<LinkModel>();
    public static List<LinkModel> AllLinks_TYPE4 = new ArrayList<LinkModel>();
    public static List<LinkModel> AllLinks_TYPE5 = new ArrayList<LinkModel>();

    public static Map<Integer, ConnectionModel> ConnectionDictionary_TYPE1_MARGINS = new HashMap<Integer, ConnectionModel>();
    public static Map<Integer, ConnectionModel> ConnectionDictionary_TYPE2_NODES = new HashMap<Integer, ConnectionModel>();
    public static Map<Integer, ConnectionModel> ConnectionDictionary_TYPE3_SPECIAL = new HashMap<Integer, ConnectionModel>();
    public static Map<Integer, ConnectionModel> ConnectionDictionary_TYPE4 = new HashMap<Integer, ConnectionModel>();
    public static Map<Integer, ConnectionModel> ConnectionDictionary_TYPE5 = new HashMap<Integer, ConnectionModel>();

    
    private Material Margin_Default;
    
    private Material Margin_Forward;
    
    private Material Margin_ForwardDouble;
    
    private Material Margin_CancelOut;

    public static Material S_Margin_Default;
    public static Material S_Margin_Forward;
    public static Material S_Margin_ForwardDouble;
    public static Material S_Margin_CancelOut;

    public static Color DefaultLinkColor_TYPE1;
    public static Color MutualLinkColor_TYPE1;

    public static Color DefaultLinkColor_TYPE2;
    public static Color DefaultLinkColor_TYPE3;

    public static Color DefaultLinkColor_TYPE4;
    public static Color MutualLinkColor_TYPE4;

    public static Color DefaultLinkColor_TYPE5;
    public static Color MutualLinkColor_TYPE5;

    public static Color NoLinkColor;
    public static Color CancelOutLinkColor;
    public static Color DefaultSecondaryColor;
    public static Color DefaultComputedPathColor;

    public class Camera{};
    
    public class GUIStyle{

		public int fontSize;
		public Object normal;
		public Object alignment;};
    
    public class Color{

		public Color(float f, float g, float h, float i) {
			// TODO Auto-generated constructor stub
		}
		public Color() {
			// TODO Auto-generated constructor stub
		}
		public Color blue = null;
		public Color yellow = null;
		public Color green = null;
		public Color white = null;
		public Color gray = null;
		public Color cyan = null;
		public Color black = null;
		public Color red = null;};
    
    private Camera Cam;
    private Rect rect;
    private GUIStyle m_gs;
    private boolean m_marked = false;
    
    public class TextAnchor{

		public Object UpperLeft = null;}

    void Awake()
    {
        S_Margin_Default = Margin_Default;
        S_Margin_Forward = Margin_Forward;
        S_Margin_ForwardDouble = Margin_ForwardDouble;
        S_Margin_CancelOut = Margin_CancelOut;

        DefaultLinkColor_TYPE1 = new Color().blue;
        DefaultLinkColor_TYPE2 = new Color().yellow;
        DefaultLinkColor_TYPE3 = new Color().green;
        DefaultLinkColor_TYPE4 = new Color(0f, 255f, 179f, 255f);
        DefaultLinkColor_TYPE5 = new Color(197f, 244f, 66f, 255f);

        DefaultSecondaryColor = new Color().white;
        NoLinkColor = new Color().gray;
        MutualLinkColor_TYPE1 = new Color().cyan;
        MutualLinkColor_TYPE4 = new Color(0f, 255f, 255f, 255f);
        MutualLinkColor_TYPE5 = new Color(235f, 180f, 0f, 255f);
        CancelOutLinkColor = new Color().black;
        DefaultComputedPathColor = new Color().red;
     
    }

    void OnEnable()
    {
        //Debug.LogError("ENABLE " + this.name);
        m_gs = new GUIStyle();
        m_gs.fontSize = 40;
//        m_gs.normal.textColor = Color.white;
        m_gs.alignment = new TextAnchor().UpperLeft;

        N_E = E = S_E = S_V = V = N_V = null;
        N = N_E_E = S_E_E = S = S_V_V = N_V_V = null;       
    }

    public void ResetMargins()
    {
        N_E_MARGIN = N_V_MARGIN = E_MARGIN = V_MARGIN = S_E_MARGIN = S_V_MARGIN = null;
    }
   
    public static void AddToList(StarModel sm, FRACTALIZATION_TYPE fType)
    {
        if(!GetAllStars(fType).contains(sm))
        {
            GetAllStars(fType).add(sm);
        }
    }

    public void SetNumber(int n)
    {
        m_localNumber = n;
    }

    public int GetNumber()
    {
        return m_localNumber;
    }

    public static void ResetAllLinkColors()
    {
        for(LinkModel lm:StarModel.GetCurrentLinks(StarModel.CurrentFType))
        {
            //Color c = DefaultLinkColor_TYPE1;
            //if(lm.GetLinkingType() == LinkModel.LINKING_TYPE.NO_LINK)
            //{
            //    c = NoLinkColor;
            //}
            //else if(lm.GetLinkingType() == LinkModel.LINKING_TYPE.FORWARD_DOUBLE)
            //{
            //    c = MutualLinkColor_TYPE1;
            //}
            //lm.SetConnectionColor(c, DefaultSecondaryColor);
            lm.UpdateLinkVisual();
        }
    }

    public static void SetLinkColor(int start, int end, Color main, Color secondary)
    {
//        LinkModel lm = StarModel.GetCurrentLinks(StarModel.CurrentFType).FirstOrDefault(obj => (obj.GetStartNode() == start && obj.GetEndNode() == end));
//
//        if(lm != null)
//        {
//            lm.SetConnectionColor(main, secondary);
//        }        
    }

    public static List<LinkModel> GetCurrentLinks(StarModel.FRACTALIZATION_TYPE fType)
    {
        switch(fType)
        {
            case BASED_ON_MARGINS:
                return StarModel.AllLinks_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return StarModel.AllLinks_TYPE2_NODES;
            case SPECIAL:
                return StarModel.AllLinks_TYPE3_SPECIAL;
            case TYPE_4:
                return StarModel.AllLinks_TYPE4;
            case TYPE_5:
                return StarModel.AllLinks_TYPE5;
        }

        return new ArrayList<LinkModel>();
    }

    public static Map<Integer, ConnectionModel> GetCurrentConnections(StarModel.FRACTALIZATION_TYPE fType)
    {
        switch (fType)
        {
            case BASED_ON_MARGINS:
                return StarModel.ConnectionDictionary_TYPE1_MARGINS;
            case BASED_ON_NODES:
                return StarModel.ConnectionDictionary_TYPE2_NODES;
            case SPECIAL:
                return StarModel.ConnectionDictionary_TYPE3_SPECIAL;
            case TYPE_4:
                return StarModel.ConnectionDictionary_TYPE4;
            case TYPE_5:
                return StarModel.ConnectionDictionary_TYPE5;
        }

        return new HashMap<Integer, ConnectionModel>();
    }
    
    //	TODO
    private static void AddNewLink(GameObject firstObj, GameObject secondObj, GameObject connection, LinkModel.LINKING_TYPE o, StarModel.FRACTALIZATION_TYPE fType)
    {
        List<LinkModel> links = GetCurrentLinks(StarModel.CurrentFType);

//        int first = firstObj.GetComponent().GetNodeNumber();
//        int second = secondObj.GetComponent().GetNodeNumber();
//
//        if (null == links.FirstOrDefault(obj=> (obj.GetConnectionObj() == connection)))
//        {
//            LinkModel lm = new LinkModel(first, second, firstObj, secondObj, connection, o);
//            connection.name = first > second ? second.toString() + "__" + first.toString() : first.toString() + "__" + second.toString();
//            links.add(lm);
//
//        }
    }

    public static void LoadDefaultConfiguration(StarModel.FRACTALIZATION_TYPE fType)
    {
//        String dcFile ="";
//
//        switch(fType)
//        {
//            case BASED_ON_MARGINS:
//                dcFile ="default_configuration_margins";
//                break;
//            case BASED_ON_NODES:
//                dcFile ="default_configuration_nodes";
//                break;
//            case SPECIAL:
//                dcFile ="default_configuration_special";
//                break;
//            case TYPE_4:
//                dcFile ="default_configuration_type4";
//                break;
//            case TYPE_5:
//                dcFile ="default_configuration_type5";
//                break;
//        }
//
//        if(dcFile.compareTo("") != 0) //bad! bad! bad!
//        {
//            string[] lines = FileUtilities.ReadAllLinesFromResources(dcFile, true);
//            for(String l:lines)
//            {
//                string[] temp = l.Split(' ');
//                int start = -1, end = -1;
//
//                if(temp.Length == 2 &&
//                   temp[1] !=null &&
//                   temp[0] != null &&
//                   int.TryParse(temp[0], out start) && 
//                   int.TryParse(temp[1], out end))
//                {
//                    StarModel.UpdateLink(start, end, LinkModel.LINKING_TYPE.FORWARD, StarModel.CurrentFType);
//                }
//            }
//        }
    }

//    public static void UpdateLink(int start, int end, LinkModel.LINKING_TYPE o, StarModel.FRACTALIZATION_TYPE fType, String linkTitle, String linkContent )
//    {
//        List<LinkModel> links = GetCurrentLinks(StarModel.CurrentFType);
//        Map<Integer, ConnectionModel> connections = GetCurrentConnections(StarModel.CurrentFType);
//
//
//        //remove end -> start everywhere (previous)
//        LinkModel lm_end_to_start = links.FirstOrDefault(obj => (((obj.GetStartNode() == end && obj.GetEndNode() == start))));
//        if (lm_end_to_start != null)
//        {
//            if (links.contains(lm_end_to_start))
//            {
//                links.remove(lm_end_to_start);
//            }
//
//            ConnectionModel cm = connections.get(end);
//            if (cm !=null && cm.Connections_FromStart.contains(lm_end_to_start))
//            {
//                cm.Connections_FromStart.remove(lm_end_to_start);
//            }
//        }
//
//        //remove start -> end everywhere (in case of linking type cancel-out change to another value
//        LinkModel lm_start_to_end = links.FirstOrDefault(obj => (((obj.GetStartNode() == start && obj.GetEndNode() == end))));
//        if (lm_start_to_end != null)
//        {
//            if (links.contains(lm_start_to_end))
//            {
//                links.remove(lm_start_to_end);
//            }
//
//            ConnectionModel cm;
//            if (connections.TryGetValue(start, out cm) && cm.Connections_FromStart.contains(lm_start_to_end))
//            {
//                cm.Connections_FromStart.remove(lm_start_to_end);
//            }
//        }
//
//        if(lm_end_to_start == null && lm_start_to_end == null)
//        {
//            Debug.LogError(" Any Connection between " + start + " and " + end + " was not found on  "+StarModel.CurrentFType );
//        }
//        else //if (o != LinkModel.LINKING_TYPE.DEFAULT) - add this and lms will be removed permanently
//        {
//            GameObject connection;
//            GameObject startObj;
//            GameObject endObj;
//
//            if(lm_start_to_end != null)
//            {
//                connection = lm_start_to_end.GetConnectionObj();
//                startObj = lm_start_to_end.GetStartObj();
//                endObj = lm_start_to_end.GetEndObj();
//
//            } 
//            else if (lm_end_to_start != null)
//            {
//                connection = lm_end_to_start.GetConnectionObj();
//                startObj = lm_end_to_start.GetEndObj();
//                endObj = lm_end_to_start.GetStartObj();
//            }
//            else
//            {
//                Debug.LogError("Error at connection between " + start + " and " + end);
//                return;
//            }
//
//            LinkModel new_lm_fw = new LinkModel(start, end, startObj, endObj, connection, o);
//            links.add(new_lm_fw);
//
//            ConnectionModel cm;
//            if (!connections.TryGetValue(start, out cm))
//            {
//                cm = new ConnectionModel();
//                connections.add(start, cm);
//            }
//            connections.remove(start);
//            cm.AddLink_FromStart(new_lm_fw);
//            
//            connections.add(start, cm);
//
//            if (o == LinkModel.LINKING_TYPE.CANCEL_OUT)
//            {
//                LinkModel new_lm_rev = new LinkModel(end, start, endObj, startObj, connection, o);
//                links.add(new_lm_rev);
//
//                ConnectionModel cm_rev;
//                if (!connections.TryGetValue(end, out cm_rev))
//                {
//                    cm_rev = new ConnectionModel();
//                    connections.add(end, cm_rev);
//                }
//
//                connections.remove(end);
//                cm_rev.AddLink_FromStart(new_lm_rev);
//                
//                connections.add(end, cm_rev);
//            }
//
//            connection.GetComponent<ContentView>().SetTitle(linkTitle);
//            connection.GetComponent<ContentView>().SetContent(linkContent); //:)
//        }
//    }

    public static void NumberStarsAndGenerateList(FRACTALIZATION_TYPE fType)
    {
        int i = 0;
        int node_i = 0;

        for(GameObject g:GetAllNodes(fType))
        {if (g!=null)
            g.
            GetComponent()
            .Reset();
        }
        for(StarModel sm:GetAllStars(fType))
        {
            //if(sm.StarEnabled)
            {
                sm.SetNumber(i++);
                if (sm.Node_2OCLOCK!=null && GetAllNodes(fType).contains(sm.Node_2OCLOCK) && sm.Node_2OCLOCK.GetComponent().GetNodeNumber()==null)
                {
                    sm.Node_2OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_2OCLOCK.name ="Node " + (node_i - 1);
                }
                if (sm.Node_4OCLOCK!=null && GetAllNodes(fType).contains(sm.Node_4OCLOCK) && sm.Node_4OCLOCK.GetComponent().GetNodeNumber()==null)
                {
                    sm.Node_4OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_4OCLOCK.name = "Node " + (node_i - 1);
                }
                if (sm.Node_6OCLOCK!=null && GetAllNodes(fType).contains(sm.Node_6OCLOCK) && sm.Node_6OCLOCK.GetComponent().GetNodeNumber() == null)
                {
                    sm.Node_6OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_6OCLOCK.name = "Node " + (node_i - 1);
                }
                if (sm.Node_8OCLOCK!=null && GetAllNodes(fType).contains(sm.Node_8OCLOCK) && sm.Node_8OCLOCK.GetComponent().GetNodeNumber() == null)
                {
                    sm.Node_8OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_8OCLOCK.name = "Node " + (node_i - 1);
                }
                if (sm.Node_10OCLOCK != null && GetAllNodes(fType).contains(sm.Node_10OCLOCK) && sm.Node_10OCLOCK.GetComponent().GetNodeNumber() == null)
                {
                    sm.Node_10OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_10OCLOCK.name = "Node " + (node_i - 1);
                }
                if (sm.Node_12OCLOCK!=null && GetAllNodes(fType).contains(sm.Node_12OCLOCK) && sm.Node_12OCLOCK.GetComponent().GetNodeNumber() == null)
                {
                    sm.Node_12OCLOCK.GetComponent().Set(node_i++);
                    sm.Node_12OCLOCK.name = "Node " + (node_i-1);
                }

                for(GameObject g:sm.CenterNodes)
                {
                    g.GetComponent().Set(node_i++);
                    g.name = "Node " + (node_i - 1);
                }

                //exterior
                //5 - 0
                AddNewLink(sm.Node_12OCLOCK, sm.Node_2OCLOCK, sm.N_E_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                //0 - 1
                AddNewLink(sm.Node_2OCLOCK, sm.Node_4OCLOCK, sm.E_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                //1 - 2
                AddNewLink(sm.Node_4OCLOCK, sm.Node_6OCLOCK, sm.S_E_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                //2 - 3
                AddNewLink(sm.Node_6OCLOCK, sm.Node_8OCLOCK, sm.S_V_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                //4 - 3
                AddNewLink(sm.Node_10OCLOCK, sm.Node_8OCLOCK, sm.V_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                //5 - 4
                AddNewLink(sm.Node_12OCLOCK, sm.Node_10OCLOCK, sm.N_V_MARGIN, LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);

               // if (StarModel.CurrentFType != FRACTALIZATION_TYPE.SPECIAL)
                {
                    //downward oriented triangle
                    //4 - 6
                    AddNewLink(sm.Node_10OCLOCK, sm.CenterNodes[0], sm.CenterMargins[0], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //6 - 7                                          
                    AddNewLink(sm.CenterNodes[0], sm.CenterNodes[1], sm.CenterMargins[1], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    // 7 - 0                                         
                    AddNewLink(sm.CenterNodes[1], sm.Node_2OCLOCK, sm.CenterMargins[2], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //0 - 8                                          
                    AddNewLink(sm.Node_2OCLOCK, sm.CenterNodes[2], sm.CenterMargins[3], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //8 - 9                                          
                    AddNewLink(sm.CenterNodes[2], sm.CenterNodes[3], sm.CenterMargins[4], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //9 - 2                                          
                    AddNewLink(sm.CenterNodes[3], sm.Node_6OCLOCK, sm.CenterMargins[5], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //2 - 10                                         
                    AddNewLink(sm.Node_6OCLOCK, sm.CenterNodes[4], sm.CenterMargins[6], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //10 - 11                                        
                    AddNewLink(sm.CenterNodes[4], sm.CenterNodes[5], sm.CenterMargins[7], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //11 - 4                                         
                    AddNewLink(sm.CenterNodes[5], sm.Node_10OCLOCK, sm.CenterMargins[8], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);


                    //upward oriented triangle
                    //3 - 11
                    AddNewLink(sm.Node_8OCLOCK, sm.CenterNodes[5], sm.CenterMargins[9], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //11 - 6
                    AddNewLink(sm.CenterNodes[5], sm.CenterNodes[0], sm.CenterMargins[10], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //6 - 5
                    AddNewLink(sm.CenterNodes[0], sm.Node_12OCLOCK, sm.CenterMargins[11], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //7 - 5
                    AddNewLink(sm.CenterNodes[1], sm.Node_12OCLOCK, sm.CenterMargins[12], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //8 - 7
                    AddNewLink(sm.CenterNodes[2], sm.CenterNodes[1], sm.CenterMargins[13], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //1 - 8
                    AddNewLink(sm.Node_4OCLOCK, sm.CenterNodes[2], sm.CenterMargins[14], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //9 - 1
                    AddNewLink(sm.CenterNodes[3], sm.Node_4OCLOCK, sm.CenterMargins[15], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //10 - 9
                    AddNewLink(sm.CenterNodes[4], sm.CenterNodes[3], sm.CenterMargins[16], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                    //3 - 10
                    AddNewLink(sm.Node_8OCLOCK, sm.CenterNodes[4], sm.CenterMargins[17], LinkModel.LINKING_TYPE.NO_LINK, StarModel.CurrentFType);
                }
            }
        }
    }

    

    public void MarginCorrection()
    {
//        Vector3 r = V_MARGIN.transform.rotation.eulerAngles;
//        r.z = 90;
//        r.y = 90;
//        V_MARGIN.transform.rotation = Quaternion.Euler(r);
//
//        r = E_MARGIN.transform.rotation.eulerAngles;
//        r.z = 90;
//        r.y = 90;
//        E_MARGIN.transform.rotation = Quaternion.Euler(r);
    }

    public static void MarginCorrectionExtended(GameObject margin)
    {

//        Vector3 r = margin.transform.rotation.eulerAngles;
//        r.z = 90;
//        r.y = 90;
//        margin.transform.rotation = Quaternion.Euler(r);
//
    }

    public void SelectiveActivateMargins(boolean activate, FRACTALIZATION_TYPE fType)
    {
//        if (fType == FRACTALIZATION_TYPE.BASED_ON_MARGINS || 
//            fType == FRACTALIZATION_TYPE.TYPE_4 || 
//            fType == FRACTALIZATION_TYPE.TYPE_5)
//        {
//            if (!activate)
//            {
//                if (!N_E || !N_E.StarEnabled)
//                {
//                    N_E_MARGIN.SetActive(false);
//                }
//                if (!E || !E.StarEnabled)
//                {
//                    E_MARGIN.SetActive(false);
//                }
//                if (!S_E || !S_E.StarEnabled)
//                {
//                    S_E_MARGIN.SetActive(false);
//                }
//                if (!S_V || !S_V.StarEnabled)
//                {
//                    S_V_MARGIN.SetActive(false);
//                }
//                if (!V || !V.StarEnabled)
//                {
//                    V_MARGIN.SetActive(false);
//                }
//                if (!N_V || !N_V.StarEnabled)
//                {
//                    N_V_MARGIN.SetActive(false);
//                }
//            }
//            else
//            {
//                //OBJECT ACTIVE:HIERARCHY!!!
//                N_E_MARGIN.SetActive(true);
//                E_MARGIN.SetActive(true);
//                S_E_MARGIN.SetActive(true);
//                S_V_MARGIN.SetActive(true);
//                V_MARGIN.SetActive(true);
//                N_V_MARGIN.SetActive(true);
//
//            }
//        }
//        else
//        {           
//                N_E_MARGIN.SetActive(activate);
//                E_MARGIN.SetActive(activate);
//                S_E_MARGIN.SetActive(activate);
//                S_V_MARGIN.SetActive(activate);
//                V_MARGIN.SetActive(activate);
//                N_V_MARGIN.SetActive(activate);
//        }
//        MarginCorrection();
//
//
//        for(GameObject g:CenterMargins)
//        {
//            g.SetActive(activate);
//        }
    }

    public GameObject SetNodes(Transform t, FRACTALIZATION_TYPE fType)
    {

        GameObject node = new GameObject(NodePrefab);
//        if (fType == FRACTALIZATION_TYPE.TYPE_5)
//        {
//            node.transform.localScale *= 0.5f;
//        }
//        node.transform.position = t.transform.position;
//        
//        node.transform.SetParent(Parent);
        GetAllNodes(fType).add(node);

//        node.transform.SetParent(Parent);

        return node;
    }

    public void ActivateNodes(boolean activate, StarModel.FRACTALIZATION_TYPE fType)
    {
//        if (fType == FRACTALIZATION_TYPE.BASED_ON_MARGINS ||
//            fType == FRACTALIZATION_TYPE.TYPE_4 ||
//            fType == FRACTALIZATION_TYPE.TYPE_5)
//        {
//            if (Node_2OCLOCK && (!N_E  || !N_E.StarEnabled) &&   (!E || !E.StarEnabled))   Node_2OCLOCK.SetActive(activate);
//            if (Node_4OCLOCK && (!E    || !E.StarEnabled) &&   (!S_E || !S_E.StarEnabled)) Node_4OCLOCK.SetActive(activate);
//            if (Node_6OCLOCK && (!S_E  || !S_E.StarEnabled) && (!S_V || !S_V.StarEnabled)) Node_6OCLOCK.SetActive(activate);
//            if (Node_8OCLOCK && (!S_V  || !S_V.StarEnabled) &&   (!V || !V.StarEnabled))   Node_8OCLOCK.SetActive(activate);
//            if (Node_10OCLOCK && (!V   || !V.StarEnabled) &&   (!N_V || !N_V.StarEnabled)) Node_10OCLOCK.SetActive(activate);
//            if (Node_12OCLOCK && (!N_V || !N_V.StarEnabled) && (!N_E || !N_E.StarEnabled)) Node_12OCLOCK.SetActive(activate);
//        }
//        else
//        {
//            if (Node_2OCLOCK && (!N_E_E  || !N_E_E.StarEnabled))  Node_2OCLOCK.SetActive(activate);
//            if (Node_4OCLOCK && (!S_E_E  || !S_E_E.StarEnabled))  Node_4OCLOCK.SetActive(activate);
//            if (Node_6OCLOCK && (!S      || !S.StarEnabled))      Node_6OCLOCK.SetActive(activate);
//            if (Node_8OCLOCK && (!S_V_V  || !S_V_V.StarEnabled))  Node_8OCLOCK.SetActive(activate);
//            if (Node_10OCLOCK && (!N_V_V || !N_V_V.StarEnabled))  Node_10OCLOCK.SetActive(activate);
//            if (Node_12OCLOCK && (!N     || !N.StarEnabled))      Node_12OCLOCK.SetActive(activate);
//        }
//
//        for(GameObject g:CenterNodes)
//        {
//            g.SetActive(activate);
//        }
    }

    public void AddCenterNodes(StarModel.FRACTALIZATION_TYPE fType)
    {
        for(GameObject g:CenterNodes)
        {
            GetAllNodes(fType).add(g);
        }
    }

    public void SetMargin(NEIGHBOR_POSITION n)
    {

//        switch (n)
//        {
//            case N_E:
//                if (N_E_MARGIN == null)
//                {
//                    N_E_MARGIN = new GameObject(N_E_MarginPrefab);
//                    N_E_MARGIN.transform.position = N_E_MarginTransform.position;
//                    SetMarginScale(N_E_MARGIN);
//                    N_E_MARGIN.transform.SetParent(this.gameObject.transform);
//                    N_E_MARGIN.SetActive(false);
//                    N_E_MARGIN.transform.SetParent(Parent);
//                    
//                }
//                if(N_E!=null)
//                N_E.S_V_MARGIN = N_E_MARGIN;
//                break;
//            case E:
//                if (E_MARGIN == null)
//                {
//                    E_MARGIN = new GameObject(E_MarginPrefab);
//                    E_MARGIN.transform.position = E_MarginTransform.position;
//                    SetMarginScale(E_MARGIN);
//                    E_MARGIN.transform.SetParent(this.gameObject.transform);
//                    E_MARGIN.SetActive(false);
//                    E_MARGIN.transform.SetParent(Parent);
//                }
//                if(E!=null)
//                E.V_MARGIN = E_MARGIN;
//                break;
//            case S_E:
//                if (S_E_MARGIN == null)
//                {
//                    S_E_MARGIN = new GameObject(S_E_MarginPrefab);
//                    S_E_MARGIN.transform.position = S_E_MarginTransform.position;
//                    SetMarginScale(S_E_MARGIN);
//                    S_E_MARGIN.transform.SetParent(this.gameObject.transform);
//                    S_E_MARGIN.SetActive(false);
//                    S_E_MARGIN.transform.SetParent(Parent);
//                }
//                if(S_E!=null)
//                S_E.N_V_MARGIN = S_E_MARGIN;
//                break;
//            case S_V:
//                if (S_V_MARGIN == null)
//                {
//                    S_V_MARGIN = new GameObject(S_V_MarginPrefab);
//                    S_V_MARGIN.transform.position = S_V_MarginTransform.position;
//                    SetMarginScale(S_V_MARGIN);
//                    S_V_MARGIN.transform.SetParent(this.gameObject.transform);
//                    S_V_MARGIN.SetActive(false);
//                    S_V_MARGIN.transform.SetParent(Parent);
//                }
//                if(S_V!=null)
//                S_V.N_E_MARGIN = S_V_MARGIN;
//                break;
//            case V:
//                if (V_MARGIN == null)
//                {
//                    V_MARGIN = new GameObject(V_MarginPrefab);
//                    V_MARGIN.transform.localPosition = V_MarginTransform.position;
//                    SetMarginScale(V_MARGIN);
//                    V_MARGIN.transform.SetParent(this.gameObject.transform);
//                    V_MARGIN.SetActive(false);
//                    V_MARGIN.transform.SetParent(Parent);
//                }
//                if(V!=null)
//                V.E_MARGIN = V_MARGIN;
//                break;
//            case N_V:
//                if (N_V_MARGIN == null)
//                {
//                    N_V_MARGIN = new GameObject(N_V_MarginPrefab);
//                    N_V_MARGIN.transform.localPosition = N_V_MarginTransform.position;
//                    SetMarginScale(N_V_MARGIN);
//                    N_V_MARGIN.transform.SetParent(this.gameObject.transform);
//                    N_V_MARGIN.SetActive(false);
//                    N_V_MARGIN.transform.SetParent(Parent);
//                }
//                if(N_V!=null)
//                N_V.S_E_MARGIN = N_V_MARGIN;
//                break;
//        }
        
    }

    private void SetMarginScale(GameObject margin)
    {
//        if (StarModel.CurrentFType == FRACTALIZATION_TYPE.SPECIAL || 
//            StarModel.CurrentFType == FRACTALIZATION_TYPE.TYPE_4)
//        {
//            Vector3 sc = margin.transform.localScale;
//            sc.z *= 0.575f;
//            margin.transform.localScale = sc;
//        }
//        else if(StarModel.CurrentFType == FRACTALIZATION_TYPE.TYPE_5)
//        {
//            Vector3 sc = margin.transform.localScale;
//            sc.z *= 0.25f;
//            margin.transform.localScale = sc;
//        }
    }

    public void SetStar(NEIGHBOR_POSITION n, FRACTALIZATION_TYPE fType)
    {
        Node_2OCLOCK = Node_4OCLOCK = Node_6OCLOCK = Node_8OCLOCK = Node_10OCLOCK = Node_12OCLOCK = null;
        
        switch (n)
        {
            case N_E:
                if (N_E == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = N_E_Transform.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();

                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    N_E = sm;

                    if (!GetAllStars(fType).contains(N_E)) GetAllStars(fType).add(N_E);
                                        
                }

                break;
            case E:
                if (E == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = E_Transorm.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    E = sm;

                    if (!GetAllStars(fType).contains(E)) GetAllStars(fType).add(E);

                }

                break;
            case S_E:
                if (S_E == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = S_E_Transform.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    S_E = sm;

                    if (!GetAllStars(fType).contains(S_E)) GetAllStars(fType).add(S_E);
                   
                }

                break;
            case S_V:
                if (S_V == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = S_V_Transform.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    S_V = sm;

                    if (!GetAllStars(fType).contains(S_V)) GetAllStars(fType).add(S_V);

                }

                break;
            case V:
                if (V == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = V_Transform.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    V = sm;

                    if (!GetAllStars(fType).contains(V)) GetAllStars(fType).add(V);

                }

                break;
            case N_V:
                if (N_V == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = N_V_Transform.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    N_V = sm;

                    if (!GetAllStars(fType).contains(N_V)) GetAllStars(fType).add(N_V);

                }

                break;
            case N:
                if (N == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = N_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    N = sm;

                    if (!GetAllStars(fType).contains(N)) GetAllStars(fType).add(N);
                }
                break;
            case N_E_E:
                if (N_E_E == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = N_E_E_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    N_E_E = sm;

                    if (!GetAllStars(fType).contains(N_E_E)) GetAllStars(fType).add(N_E_E);
                }
                break;
            case S_E_E:
                if (S_E_E == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = S_E_E_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    S_E_E = sm;

                    if (!GetAllStars(fType).contains(S_E_E)) GetAllStars(fType).add(S_E_E);
                }
                break;
            case S:
                if (S == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = S_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    S = sm;

                    if (!GetAllStars(fType).contains(S)) GetAllStars(fType).add(S);
                }
                break;
            case S_V_V:
                if (S_V_V == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = S_V_V_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    S_V_V = sm;

                    if (!GetAllStars(fType).contains(S_V_V)) GetAllStars(fType).add(S_V_V);
                }
                break;
            case N_V_V:
                if (N_V_V == null)
                {
                    ThisGO = new GameObject(OriginalStarObject);
                    ThisGO.name = (++count)+"";
                    ThisGO.transform.position = N_V_V_Transform_n.position;
                    StarModel sm = this;//ThisGO.GetComponentStarModel();
                    ThisGO.transform.SetParent(Parent);

                    sm.Star.SetActive(false);
                    sm.Sphere.SetActive(true);
                    sm.ResetMargins();
                    N_V_V = sm;

                    if (!GetAllStars(fType).contains(N_V_V)) GetAllStars(fType).add(N_V_V);
                }
                break;
        }

        
    }

    public class Vector3{}
    void DisplayTextAt(String text, Vector3 position, Color c)
    {
//        m_screenPos = Cam.WorldToScreenPoint(position);
//        m_screenPos.y = Screen.height - m_screenPos.y;
//        m_gs.normal.textColor = c;
//
//        rect = new Rect(m_screenPos, Vector2.one * 50);
//        m_gs.fontSize = (int)FontSizeSliderView.SliderValue;
//        GUI.Label(rect, text, m_gs);
    }
    public static boolean ShowLabels = true;

    void OnGUI()
    {
//        if (ShowLabels && StarEnabled && Sphere.activeInHierarchy)
//        {
//            //m_screenPos = Cam.WorldToScreenPoint(this.transform.position);
//            //m_screenPos.y = Screen.height - m_screenPos.y;
//
//            //rect = new Rect(m_screenPos, Vector2.one * 50);
//            //GUI.Label(rect, m_localNumber.toString(), m_gs);
//
//            DisplayTextAt(m_localNumber.toString(), this.transform.position, Color.white);
//
//            DisplayTextAt(Node_12OCLOCK.GetComponent().GetNodeNumber().toString(), Node_12OCLOCK.transform.position, Color.red);
//            DisplayTextAt(Node_2OCLOCK.GetComponent().GetNodeNumber().toString(),  Node_2OCLOCK.transform.position,  Color.red);
//            DisplayTextAt(Node_4OCLOCK.GetComponent().GetNodeNumber().toString(),  Node_4OCLOCK.transform.position,  Color.red);
//            DisplayTextAt(Node_6OCLOCK.GetComponent().GetNodeNumber().toString(),  Node_6OCLOCK.transform.position,  Color.red);
//            DisplayTextAt(Node_8OCLOCK.GetComponent().GetNodeNumber().toString(),  Node_8OCLOCK.transform.position,  Color.red);
//            DisplayTextAt(Node_10OCLOCK.GetComponent().GetNodeNumber().toString(), Node_10OCLOCK.transform.position, Color.red);
//
//            for(GameObject g:CenterNodes)
//            {
//                DisplayTextAt(g.GetComponent().GetNodeNumber().toString(), g.transform.position, Color.green);
//            }
//        }
    }

    void OnDisable()
    {

    }

    public static void SwapStructure(FRACTALIZATION_TYPE fType, boolean activateColliders, float alpha)
    {
//        SetStructureAlphaValue(fType, alpha);
//        for(StarModel sm:GetAllStars(fType))
//        {
//            sm.Sphere.SetActive(activateColliders);
//            
//        }
//
//        for(GameObject g:GetAllNodes(fType))
//        {
//            g.GetComponent<Collider>().enabled = activateColliders;
//            //g.GetComponent<Collider>().enabled = activateColliders;
//            Color c = g.GetComponent<Renderer>().material.GetColor("_Color");
//            c.a = alpha;
//            g.GetComponent<Renderer>().material.SetColor("_Color", c);
//            g.GetComponent().SetAccumTransparency(alpha);
//        }
//
//        for(LinkModel lm:GetCurrentLinks(fType))
//        {
//            lm.GetConnectionObj().GetComponent<Collider>().enabled = activateColliders;
//            lm.GetConnectionObj().GetComponent<SetParallaxParametersViewModel>().SetMaterialAlpha(alpha);
//        }

    }

    public static void SetStructureTransparency(FRACTALIZATION_TYPE fType, float alpha)
    {
//        SetStructureAlphaValue(fType, alpha);
//        for(GameObject g:GetAllNodes(fType))
//        {          
//            Color c = g.GetComponent<Renderer>().material.GetColor("_Color");
//            c.a = alpha;
//            g.GetComponent<Renderer>().material.SetColor("_Color", c);
//
//            g.GetComponent().SetAccumTransparency(alpha);
//        }
//
//        for(LinkModel lm:GetCurrentLinks(fType))
//        {
//            lm.GetConnectionObj().GetComponent<SetParallaxParametersViewModel>().SetMaterialAlpha(alpha);
//        }
    }

    //public static void ShowAccumulations(FRACTALIZATION_TYPE fType)
    //{

        
        
    //}
    
    public static void ShowAccumulations(FRACTALIZATION_TYPE fType)
    {
//        for(GameObject g:GetAllNodes(fType))
//        {
//            if (g.activeInHierarchy)
//            {
//                int val = g.GetComponent().GetNodeNumber();
//                int to_val_count = 0;
//                int from_val_count = 0;
//
//                for(LinkModel lm:GetCurrentLinks(fType))
//                {
//                    if (lm.GetEndNode() == val && lm.GetLinkingType() != LinkModel.LINKING_TYPE.NO_LINK && lm.GetConnectionObj().activeInHierarchy)
//                    {
//                        to_val_count++;
//                    }
//
//                    if (lm.GetStartNode() == val && lm.GetLinkingType() != LinkModel.LINKING_TYPE.NO_LINK && lm.GetConnectionObj().activeInHierarchy)
//                    {
//                        from_val_count++;
//                    }
//                }
//                //if(val == 3)
//
//                //Debug.LogError(val + " : TO - " + to_val_count + " FROM - " + from_val_count);
//
//
//                g.GetComponent().ActivateAccum(to_val_count > from_val_count);
//            }
//
//        }
    }

}

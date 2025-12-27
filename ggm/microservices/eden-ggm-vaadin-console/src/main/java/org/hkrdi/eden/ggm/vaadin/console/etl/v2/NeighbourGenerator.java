package org.hkrdi.eden.ggm.vaadin.console.etl.v2;

import org.hkrdi.eden.ggm.vaadin.console.etl.v2.StarModel.FRACTALIZATION_TYPE;

import java.util.ArrayList;
import java.util.List;

public class NeighbourGenerator extends MonoBehaviour{

    private StarModel.FRACTALIZATION_TYPE FType = FRACTALIZATION_TYPE.BASED_ON_MARGINS;

    
    private StarModel Root = new StarModel();
    
    private List<LinkModel> Links = new ArrayList<LinkModel>();
    
    private GameObject NextGenerator = new GameObject();

    private int depth = 0;
    private  int MAX_DEPTH = 2;
    private  int MAX_DEPTH_SPECIAL = 4;
    private  int MAX_DEPTH_TYPE_5 = 4;

    private static int m_starCount = 0;

    void Awake()
    {
        if (FType != StarModel.FRACTALIZATION_TYPE.TYPE_5)
        {
            Root.gameObject.SetActive(true);
        }
    }

    void OnEnable()
    {
        //if (FType != StarModel.FRACTALIZATION_TYPE.TYPE_5)
        {
            Root.name = "0";

            Root.transform.SetParent(Root.Parent);

            StarModel.CurrentFType = FType;

            Links = StarModel.GetCurrentLinks(StarModel.CurrentFType);


            int maxDepth = MAX_DEPTH;
            if(StarModel.CurrentFType == StarModel.FRACTALIZATION_TYPE.SPECIAL ||
               StarModel.CurrentFType == StarModel.FRACTALIZATION_TYPE.TYPE_4)
            {
                maxDepth = MAX_DEPTH_SPECIAL;
            }
            else if(StarModel.CurrentFType == StarModel.FRACTALIZATION_TYPE.TYPE_5)
            {
                maxDepth = MAX_DEPTH_TYPE_5;
            }

            StarModel.AddToList(Root, StarModel.CurrentFType);
            Generate(Root,
                     depth,
                     maxDepth,
                     StarModel.CurrentFType);


            Root.StarEnabled = true;
            Root.SelectiveActivateMargins(true, StarModel.CurrentFType);
            Root.ActivateNodes(true, StarModel.CurrentFType);

            StarModel.NumberStarsAndGenerateList(StarModel.CurrentFType);
            Root.MarginCorrection();

            StarModel.LoadDefaultConfiguration(StarModel.CurrentFType);
            Root.MarginCorrection();

            CheckMarginObjects(Root);

            if (NextGenerator != null)
            {
                NextGenerator.SetActive(true);
            }

            StarModel.InitializeNodeDictionaries();

            StarModel.CurrentFType = FType;
        }
    }

    // Use this for initialization
    void Generate (StarModel sm, int depth, int maxDepth, StarModel.FRACTALIZATION_TYPE fType) 
    {

        switch(FType)
        {
            case BASED_ON_MARGINS:
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.E,   fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_V, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.V ,  fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_V, fType);

                sm.N_E.S_V = sm;
                sm.E.V = sm;
                sm.S_E.N_V = sm;
                sm.S_V.N_E = sm;
                sm.V.E = sm;
                sm.N_V.S_E = sm;

                sm.N_E.S_E = sm.E;
                sm.E.N_V = sm.N_E;

                sm.E.S_V = sm.S_E;
                sm.S_E.N_E = sm.E;

                sm.S_E.V = sm.S_V;
                sm.S_V.E = sm.S_E;

                sm.S_V.N_V = sm.V;
                sm.V.S_E = sm.S_V;

                sm.V.N_E = sm.N_V;
                sm.N_V.S_V = sm.V;

                sm.N_V.E = sm.N_E;
                sm.N_E.V = sm.N_V;

                SetAllMargins(sm);
                AssembleNodes(sm, fType);
       
                sm.E.Node_10OCLOCK = sm.N_E.Node_6OCLOCK = sm.Node_2OCLOCK;
                sm.E.Node_8OCLOCK = sm.S_E.Node_12OCLOCK = sm.Node_4OCLOCK;
                sm.S_E.Node_10OCLOCK = sm.S_V.Node_2OCLOCK = sm.Node_6OCLOCK;
                sm.S_V.Node_12OCLOCK = sm.V.Node_4OCLOCK = sm.Node_8OCLOCK;
                sm.V.Node_2OCLOCK = sm.N_V.Node_6OCLOCK = sm.Node_10OCLOCK;
                sm.N_V.Node_4OCLOCK = sm.N_E.Node_8OCLOCK = sm.Node_12OCLOCK;

                m_starCount++;

                if (depth < maxDepth)
                {
                    Generate(sm.N_E, depth + 1, maxDepth, fType);
                    Generate(sm.E, depth + 1, maxDepth, fType);
                    Generate(sm.S_E, depth + 1, maxDepth, fType);
                    Generate(sm.S_V, depth + 1, maxDepth, fType);
                    Generate(sm.V, depth + 1, maxDepth, fType);
                    Generate(sm.N_V, depth + 1, maxDepth, fType);
                }
                else
                {
                    SetAllMargins(sm.N_E);
                    SetAllMargins(sm.N_V);
                    SetAllMargins(sm.E);
                    SetAllMargins(sm.S_E);
                    SetAllMargins(sm.S_V);
                    SetAllMargins(sm.V);

                    AssembleNodes(sm.N_E, fType);
                    //Destroy(sm.E.Node_12OCLOCK);
                    sm.E.Node_12OCLOCK = sm.N_E.Node_4OCLOCK;

                    AssembleNodes(sm.E, fType);
                    //Destroy(sm.S_E.Node_2OCLOCK);
                    sm.S_E.Node_2OCLOCK = sm.E.Node_6OCLOCK;

                    AssembleNodes(sm.S_E, fType);
                    //Destroy(sm.S_V.Node_4OCLOCK);
                    sm.S_V.Node_4OCLOCK = sm.S_E.Node_8OCLOCK;

                    AssembleNodes(sm.S_V, fType);
                    //Destroy(sm.V.Node_6OCLOCK);
                    sm.V.Node_6OCLOCK = sm.S_V.Node_10OCLOCK;

                    AssembleNodes(sm.V, fType);
                    //Destroy(sm.N_V.Node_8OCLOCK);
                    sm.N_V.Node_8OCLOCK = sm.V.Node_12OCLOCK;

                    AssembleNodes(sm.N_V, fType);
                    //Destroy(sm.N_V.Node_2OCLOCK);
                    sm.N_V.Node_2OCLOCK = sm.N_E.Node_10OCLOCK;
                    
                }
                //endregion
                break;
            case BASED_ON_NODES:
                //region NODES
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_V_V, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_V_V, fType);

                sm.N.S = sm;
                sm.N_E_E.S_V_V = sm;
                sm.S_E_E.N_V_V = sm;
                sm.S.N = sm;
                sm.S_V_V.N_E_E = sm;
                sm.N_V_V.S_E_E = sm;

                sm.N.S_E_E = sm.N_E_E;
                sm.N_E_E.N_V_V = sm.N;

                sm.N_E_E.S = sm.S_E_E;
                sm.S_E_E.N = sm.N_E_E;

                sm.S.N_E_E = sm.S_E_E;
                sm.S_E_E.S_V_V = sm.S;

                sm.S.N_V_V = sm.S_V_V;
                sm.S_V_V.S_E_E = sm.S;

                sm.S_V_V.N = sm.N_V_V;
                sm.N_V_V.S = sm.S_V_V;

                sm.N_V_V.N_E_E = sm.N;
                sm.N.S_V_V = sm.N_V_V;

                SetAllMargins(sm);
                AssembleNodes(sm, fType);

                sm.N.Node_6OCLOCK = sm.Node_12OCLOCK;
                sm.N_E_E.Node_8OCLOCK = sm.Node_2OCLOCK;
                sm.S_E_E.Node_10OCLOCK = sm.Node_4OCLOCK;
                sm.S.Node_12OCLOCK = sm.Node_6OCLOCK;
                sm.S_V_V.Node_2OCLOCK = sm.Node_8OCLOCK;
                sm.N_V_V.Node_4OCLOCK = sm.Node_10OCLOCK;

                m_starCount++;

                if (depth < maxDepth)
                {
                    Generate(sm.N, depth + 1, maxDepth, fType);
                    Generate(sm.N_E_E, depth + 1, maxDepth, fType);
                    Generate(sm.S_E_E, depth + 1, maxDepth, fType);
                    Generate(sm.S, depth + 1, maxDepth, fType);
                    Generate(sm.S_V_V, depth + 1, maxDepth, fType);
                    Generate(sm.N_V_V, depth + 1, maxDepth, fType);
                }
                else
                {
                    SetAllMargins(sm.N);
                    SetAllMargins(sm.N_E_E);
                    SetAllMargins(sm.S_E_E);
                    SetAllMargins(sm.S);
                    SetAllMargins(sm.S_V_V);
                    SetAllMargins(sm.N_V_V);

                    AssembleNodes(sm.N_V_V, fType);
                    //Destroy(sm.N.Node_8OCLOCK);
                    sm.N.Node_8OCLOCK = sm.N_V_V.Node_2OCLOCK;

                    AssembleNodes(sm.N, fType);
                    //Destroy(sm.N_E_E.Node_10OCLOCK);
                    sm.N_E_E.Node_10OCLOCK = sm.N.Node_4OCLOCK;

                    AssembleNodes(sm.N_E_E, fType);
                    //Destroy(sm.S_E_E.Node_12OCLOCK);
                    sm.S_E_E.Node_12OCLOCK = sm.N_E_E.Node_6OCLOCK;

                    AssembleNodes(sm.S_E_E, fType);
                    //Destroy(sm.S.Node_2OCLOCK);
                    sm.S.Node_2OCLOCK = sm.S_E_E.Node_8OCLOCK;

                    AssembleNodes(sm.S, fType);
                    //Destroy(sm.S_V_V.Node_4OCLOCK);
                    sm.S_V_V.Node_4OCLOCK = sm.S.Node_10OCLOCK;

                    AssembleNodes(sm.S_V_V, fType);
                    //Destroy(sm.N_V_V.Node_6OCLOCK);
                    sm.N_V_V.Node_6OCLOCK = sm.S_V_V.Node_12OCLOCK;

                    
                }


                    //endregion
                break;
            case SPECIAL:
                //region SPECIAL

                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E_E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E_E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S_V_V, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N_V_V, fType);

                    sm.N.S = sm;
                    sm.N_E_E.S_V_V = sm;
                    sm.S_E_E.N_V_V = sm;
                    sm.S.N = sm;
                    sm.S_V_V.N_E_E = sm;
                    sm.N_V_V.S_E_E = sm;

                    sm.N.S_E_E = sm.N_E_E;
                    sm.N_E_E.N_V_V = sm.N;

                    sm.N_E_E.S = sm.S_E_E;
                    sm.S_E_E.N = sm.N_E_E;

                    sm.S.N_E_E = sm.S_E_E;
                    sm.S_E_E.S_V_V = sm.S;

                    sm.S.N_V_V = sm.S_V_V;
                    sm.S_V_V.S_E_E = sm.S;

                    sm.S_V_V.N = sm.N_V_V;
                    sm.N_V_V.S = sm.S_V_V;

                    sm.N_V_V.N_E_E = sm.N;
                    sm.N.S_V_V = sm.N_V_V;

                    SetAllMargins(sm);
                    AssembleNodes(sm, fType);

                    sm.N.Node_6OCLOCK = sm.Node_12OCLOCK;
                    sm.N_E_E.Node_8OCLOCK = sm.Node_2OCLOCK;
                    sm.S_E_E.Node_10OCLOCK = sm.Node_4OCLOCK;
                    sm.S.Node_12OCLOCK = sm.Node_6OCLOCK;
                    sm.S_V_V.Node_2OCLOCK = sm.Node_8OCLOCK;
                    sm.N_V_V.Node_4OCLOCK = sm.Node_10OCLOCK;

                    m_starCount++;

                    if (depth < maxDepth)
                    {
                        Generate(sm.N, depth + 1, maxDepth, fType);
                        Generate(sm.N_E_E, depth + 1, maxDepth, fType);
                        Generate(sm.S_E_E, depth + 1, maxDepth, fType);
                        Generate(sm.S, depth + 1, maxDepth, fType);
                        Generate(sm.S_V_V, depth + 1, maxDepth, fType);
                        Generate(sm.N_V_V, depth + 1, maxDepth, fType);
                    }
                    else
                    {
                        SetAllMargins(sm.N);
                        SetAllMargins(sm.N_E_E);
                        SetAllMargins(sm.S_E_E);
                        SetAllMargins(sm.S);
                        SetAllMargins(sm.S_V_V);
                        SetAllMargins(sm.N_V_V);

                        AssembleNodes(sm.N_V_V, fType);
                        //Destroy(sm.N.Node_8OCLOCK);
                        sm.N.Node_8OCLOCK = sm.N_V_V.Node_2OCLOCK;

                        AssembleNodes(sm.N, fType);
                        //Destroy(sm.N_E_E.Node_10OCLOCK);
                        sm.N_E_E.Node_10OCLOCK = sm.N.Node_4OCLOCK;

                        AssembleNodes(sm.N_E_E, fType);
                        //Destroy(sm.S_E_E.Node_12OCLOCK);
                        sm.S_E_E.Node_12OCLOCK = sm.N_E_E.Node_6OCLOCK;

                        AssembleNodes(sm.S_E_E, fType);
                        //Destroy(sm.S.Node_2OCLOCK);
                        sm.S.Node_2OCLOCK = sm.S_E_E.Node_8OCLOCK;

                        AssembleNodes(sm.S, fType);
                        //Destroy(sm.S_V_V.Node_4OCLOCK);
                        sm.S_V_V.Node_4OCLOCK = sm.S.Node_10OCLOCK;

                        AssembleNodes(sm.S_V_V, fType);
                        //Destroy(sm.N_V_V.Node_6OCLOCK);
                        sm.N_V_V.Node_6OCLOCK = sm.S_V_V.Node_12OCLOCK;

                        
                    }


                    //endregion
                break;
            case TYPE_4:
                //region TYPE 4
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.S_V, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.V, fType);
                sm.SetStar(StarModel.NEIGHBOR_POSITION.N_V, fType);

                sm.N_E.S_V = sm;
                sm.E.V = sm;
                sm.S_E.N_V = sm;
                sm.S_V.N_E = sm;
                sm.V.E = sm;
                sm.N_V.S_E = sm;

                sm.N_E.S_E = sm.E;
                sm.E.N_V = sm.N_E;

                sm.E.S_V = sm.S_E;
                sm.S_E.N_E = sm.E;

                sm.S_E.V = sm.S_V;
                sm.S_V.E = sm.S_E;

                sm.S_V.N_V = sm.V;
                sm.V.S_E = sm.S_V;

                sm.V.N_E = sm.N_V;
                sm.N_V.S_V = sm.V;

                sm.N_V.E = sm.N_E;
                sm.N_E.V = sm.N_V;

                SetAllMargins(sm);
                AssembleNodes(sm, fType);

                sm.E.Node_10OCLOCK = sm.N_E.Node_6OCLOCK = sm.Node_2OCLOCK;
                sm.E.Node_8OCLOCK = sm.S_E.Node_12OCLOCK = sm.Node_4OCLOCK;
                sm.S_E.Node_10OCLOCK = sm.S_V.Node_2OCLOCK = sm.Node_6OCLOCK;
                sm.S_V.Node_12OCLOCK = sm.V.Node_4OCLOCK = sm.Node_8OCLOCK;
                sm.V.Node_2OCLOCK = sm.N_V.Node_6OCLOCK = sm.Node_10OCLOCK;
                sm.N_V.Node_4OCLOCK = sm.N_E.Node_8OCLOCK = sm.Node_12OCLOCK;

                m_starCount++;

                if (depth < maxDepth)
                {
                    Generate(sm.N_E, depth + 1, maxDepth, fType);
                    Generate(sm.E, depth + 1, maxDepth, fType);
                    Generate(sm.S_E, depth + 1, maxDepth, fType);
                    Generate(sm.S_V, depth + 1, maxDepth, fType);
                    Generate(sm.V, depth + 1, maxDepth, fType);
                    Generate(sm.N_V, depth + 1, maxDepth, fType);
                }
                else
                {
                    SetAllMargins(sm.N_E);
                    SetAllMargins(sm.N_V);
                    SetAllMargins(sm.E);
                    SetAllMargins(sm.S_E);
                    SetAllMargins(sm.S_V);
                    SetAllMargins(sm.V);

                    AssembleNodes(sm.N_E, fType);
                    //Destroy(sm.E.Node_12OCLOCK);
                    sm.E.Node_12OCLOCK = sm.N_E.Node_4OCLOCK;

                    AssembleNodes(sm.E, fType);
                    //Destroy(sm.S_E.Node_2OCLOCK);
                    sm.S_E.Node_2OCLOCK = sm.E.Node_6OCLOCK;

                    AssembleNodes(sm.S_E, fType);
                    //Destroy(sm.S_V.Node_4OCLOCK);
                    sm.S_V.Node_4OCLOCK = sm.S_E.Node_8OCLOCK;

                    AssembleNodes(sm.S_V, fType);
                    //Destroy(sm.V.Node_6OCLOCK);
                    sm.V.Node_6OCLOCK = sm.S_V.Node_10OCLOCK;

                    AssembleNodes(sm.V, fType);
                    //Destroy(sm.N_V.Node_8OCLOCK);
                    sm.N_V.Node_8OCLOCK = sm.V.Node_12OCLOCK;

                    AssembleNodes(sm.N_V, fType);
                    //Destroy(sm.N_V.Node_2OCLOCK);
                    sm.N_V.Node_2OCLOCK = sm.N_E.Node_10OCLOCK;

                }
                //endregion
                break;
            case TYPE_5:
                //region TYPE 5
                if(depth == 0)
                {
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E, fType);

                    sm.N_E.S_V = sm;
                    sm.E.V = sm;
                    sm.S_E.N_V = sm;

                    sm.N_E.S_E = sm.E;
                    sm.E.N_V = sm.N_E;

                    sm.E.S_V = sm.S_E;
                    sm.S_E.N_E = sm.E;

                    SetAllMargins(sm);//problematic?
                    AssembleNodes(sm, fType);//problematic?

                    sm.E.Node_10OCLOCK = sm.N_E.Node_6OCLOCK = sm.Node_2OCLOCK;
                    sm.E.Node_8OCLOCK = sm.S_E.Node_12OCLOCK = sm.Node_4OCLOCK;

                    sm.S_E.Node_10OCLOCK = sm.Node_6OCLOCK;
                    sm.N_E.Node_8OCLOCK = sm.Node_12OCLOCK;

                    m_starCount++;

                    Generate(sm, depth + 1, maxDepth, fType);
                    Generate(sm.N_E, depth + 1, maxDepth, fType);
                    Generate(sm.E, depth + 1, maxDepth, fType);
                    Generate(sm.S_E, depth + 1, maxDepth, fType);

//                    sm.N_E.Sphere.GetComponent<ClickOnSecondarySphere>().ForceActivation(true, fType);
//                    sm.E.Sphere.GetComponent<ClickOnSecondarySphere>().ForceActivation(true, fType);
//                    //s.Sphere.GetComponent<ClickOnSecondarySphere>().ForceActivation(StarModel.s_SaveableData.GetStarData(fType).ActivatedStars.Contains(s.GetNumber()), fType);

                }
                else
                {
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N_E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S_E, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.S_V, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.V, fType);
                    sm.SetStar(StarModel.NEIGHBOR_POSITION.N_V, fType);

                    sm.N_E.S_V = sm;
                    sm.E.V = sm;
                    sm.S_E.N_V = sm;
                    sm.S_V.N_E = sm;
                    sm.V.E = sm;
                    sm.N_V.S_E = sm;

                    sm.N_E.S_E = sm.E;
                    sm.E.N_V = sm.N_E;

                    sm.E.S_V = sm.S_E;
                    sm.S_E.N_E = sm.E;

                    sm.S_E.V = sm.S_V;
                    sm.S_V.E = sm.S_E;

                    sm.S_V.N_V = sm.V;
                    sm.V.S_E = sm.S_V;

                    sm.V.N_E = sm.N_V;
                    sm.N_V.S_V = sm.V;

                    sm.N_V.E = sm.N_E;
                    sm.N_E.V = sm.N_V;

                    SetAllMargins(sm);
                    AssembleNodes(sm, fType);

                    sm.E.Node_10OCLOCK = sm.N_E.Node_6OCLOCK = sm.Node_2OCLOCK;
                    sm.E.Node_8OCLOCK = sm.S_E.Node_12OCLOCK = sm.Node_4OCLOCK;
                    sm.S_E.Node_10OCLOCK = sm.S_V.Node_2OCLOCK = sm.Node_6OCLOCK;
                    sm.S_V.Node_12OCLOCK = sm.V.Node_4OCLOCK = sm.Node_8OCLOCK;
                    sm.V.Node_2OCLOCK = sm.N_V.Node_6OCLOCK = sm.Node_10OCLOCK;
                    sm.N_V.Node_4OCLOCK = sm.N_E.Node_8OCLOCK = sm.Node_12OCLOCK;

                    m_starCount++;

                    if (depth < maxDepth)
                    {
                        Generate(sm.N_E, depth + 1, maxDepth, fType);
                        Generate(sm.E, depth + 1, maxDepth, fType);
                        Generate(sm.S_E, depth + 1, maxDepth, fType);
                        Generate(sm.S_V, depth + 1, maxDepth, fType);
                        Generate(sm.V, depth + 1, maxDepth, fType);
                        Generate(sm.N_V, depth + 1, maxDepth, fType);
                    }
                    else
                    {
                        SetAllMargins(sm.N_E);
                        SetAllMargins(sm.N_V);
                        SetAllMargins(sm.E);
                        SetAllMargins(sm.S_E);
                        SetAllMargins(sm.S_V);
                        SetAllMargins(sm.V);

                        AssembleNodes(sm.N_E, fType);
                        //Destroy(sm.E.Node_12OCLOCK);
                        sm.E.Node_12OCLOCK = sm.N_E.Node_4OCLOCK;

                        AssembleNodes(sm.E, fType);
                        //Destroy(sm.S_E.Node_2OCLOCK);
                        sm.S_E.Node_2OCLOCK = sm.E.Node_6OCLOCK;

                        AssembleNodes(sm.S_E, fType);
                        //Destroy(sm.S_V.Node_4OCLOCK);
                        sm.S_V.Node_4OCLOCK = sm.S_E.Node_8OCLOCK;

                        AssembleNodes(sm.S_V, fType);
                        //Destroy(sm.V.Node_6OCLOCK);
                        sm.V.Node_6OCLOCK = sm.S_V.Node_10OCLOCK;

                        AssembleNodes(sm.V, fType);
                        //Destroy(sm.N_V.Node_8OCLOCK);
                        sm.N_V.Node_8OCLOCK = sm.V.Node_12OCLOCK;

                        AssembleNodes(sm.N_V, fType);
                        //Destroy(sm.N_V.Node_2OCLOCK);
                        sm.N_V.Node_2OCLOCK = sm.N_E.Node_10OCLOCK;

                    }
                }
                //endregion
                break;
        }

        

    }

    private void SetAllMargins(StarModel sm)
    {     
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.N_E);
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.E);
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.S_E);
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.S_V);
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.V);
        sm.SetMargin(StarModel.NEIGHBOR_POSITION.N_V);      
    }
	
    private void AssembleNodes(StarModel sm, StarModel.FRACTALIZATION_TYPE fType)
    {
        if (sm.Node_2OCLOCK == null) sm.Node_2OCLOCK = sm.SetNodes(sm.Node_2OCLOCK_Transform, fType);
        if (sm.Node_4OCLOCK == null) sm.Node_4OCLOCK = sm.SetNodes(sm.Node_4OCLOCK_Transform, fType);
        if (sm.Node_6OCLOCK == null) sm.Node_6OCLOCK = sm.SetNodes(sm.Node_6OCLOCK_Transform, fType);
        if (sm.Node_8OCLOCK == null) sm.Node_8OCLOCK = sm.SetNodes(sm.Node_8OCLOCK_Transform, fType);
        if (sm.Node_10OCLOCK == null) sm.Node_10OCLOCK = sm.SetNodes(sm.Node_10OCLOCK_Transform, fType);
        if (sm.Node_12OCLOCK == null) sm.Node_12OCLOCK = sm.SetNodes(sm.Node_12OCLOCK_Transform, fType);

        sm.AddCenterNodes(fType);
    }

    void CheckMarginObjects(StarModel sm)
    {
        //Debug.LogWarning("-------------------------------------");
        //Debug.LogError(sm.N_E_MARGIN.name);
        //Debug.LogError(sm.N_V_MARGIN.name);
        //Debug.LogError(sm.S_E_MARGIN.name);
        //Debug.LogError(sm.E_MARGIN.name);
        //Debug.LogError(sm.S_V_MARGIN.name);
        //Debug.LogError(sm.V_MARGIN.name);
        //Debug.LogWarning("-------------------------------------");
    }

	// Update is called once per frame
	void Update () 
    {
	
	}

	public static void main(String[] args) {
		new NeighbourGenerator().OnEnable();
	}
}

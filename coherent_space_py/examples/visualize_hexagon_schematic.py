import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Polygon
from coherent_space_py.model.enums import (
    StreamApplicationType, 
    InformationalStreamVectorDirection
)
from coherent_space_py.model.topology_rules import (
    get_upstream_connections,
    get_downstream_connections
)

VD = InformationalStreamVectorDirection
SAT = StreamApplicationType

def visualize_schematics():
    # Setup Figure with 3x2 subplots (Row 3 for Superposition)
    fig, axes = plt.subplots(3, 2, figsize=(16, 24))
    fig.suptitle('Hexavalent Logic Schematics ("Star of David" & "Inner Hexagon")', fontsize=16)
    
    # Configurations
    configs = [
        # Row 0: Upstream
        {"title": "Upstream Hexagon (CornerParity Mode)", "mode": "UP", "vd": VD.CornerParity, "ax": axes[0, 0]},
        {"title": "Upstream Hexagon (SideParity Mode)", "mode": "UP", "vd": VD.SideParity, "ax": axes[0, 1]},
        # Row 1: Downstream
        {"title": "Downstream Hexagon (CornerParity Mode)", "mode": "DOWN", "vd": VD.CornerParity, "ax": axes[1, 0]},
        {"title": "Downstream Hexagon (SideParity Mode)", "mode": "DOWN", "vd": VD.SideParity, "ax": axes[1, 1]},
        # Row 2: Combined (Superposition)
        {"title": "Superposition (CornerParity Mode)", "mode": "BOTH", "vd": VD.CornerParity, "ax": axes[2, 0]},
        {"title": "Superposition (SideParity Mode)", "mode": "BOTH", "vd": VD.SideParity, "ax": axes[2, 1]},
    ]
    
    # Geometry Definitions (Angles in degrees)
    # Upstream: Vertical orientation. 
    # Functions Triangle: Top (90), Bottom-Right (330), Bottom-Left (210)
    # Systems Triangle: Bottom (270), Top-Right (30), Top-Left (150)
    # Or variations depending on CDS/SDC.
    
    # Updated Angles based on Logical Adjacency (CS -> DF on side) and Star of David
    # Ring Order (CW from Top): SF -> CS -> DF -> SS -> CF -> DS -> SF
    # This ensures F-S-F-S-F-S alternation and valid perimeter connections.
    
    upstream_angles = {
        SAT.UpstreamSelectorFunction: 90,   # Top
        SAT.UpstreamConsumerSystem: 30,     # Top-Right
        SAT.UpstreamDetectorFunction: 330,  # Bottom-Right (-30)
        SAT.UpstreamSelectorSystem: 270,    # Bottom (-90)
        SAT.UpstreamConsumerFunction: 210,  # Bottom-Left (-150)
        SAT.UpstreamDetectorSystem: 150     # Top-Left
    }
    
    # Downstream: Logical "Gap" Hexagon
    # Rotated 30 degrees? Or aligned?
    # Downstream is "Lateral".
    # Ring: SF(Lateral?) -> ...
    # Let's align Downstream SF to 0 (Right) as lateral apex.
    # Ring Order: SF -> CS -> DF -> SS -> CF -> DS?
    # Angles: 0, 300, 240, 180, 120, 60?
    
    # Downstream: "Lateral" Inverted Hexagon (based on Java RootsOfUnity analysis)
    # SF uses get7RootOf12 -> 180 (Left)
    # SS uses get1RootOf12 -> 0 (Right)
    # Ring Order (CW from Right/SS): SS -> DS -> CF -> SF -> CS -> DF -> SS
    # Checks: 
    #   SS(0)   -> DS(60-TopRight) & DF(300-BotRight) ? Rules: SS->[DF, DS]. Yes.
    #   SF(180) -> CF(120-TopLeft) & CS(240-BotLeft)  ? Rules: SF->[CF, CS]. Yes.
    
    # Downstream: "Lateral" Hexagon (Reverted to Step 645 Geometry)
    # SF = 0 (Right), SS = 180 (Left)
    # Fix Directions: Swap CF/DS and CS/DF to reverse horizontal arrows.
    # New Layout:
    # Right (0): SF
    # Top-Right (60): CF  (Was DS)
    # Top-Left (120): DS  (Was CF)
    # Left (180): SS
    # Bottom-Left (240): CS (Was DF)
    # Bottom-Right (300): DF (Was CS)
    
    # Downstream: "Mirrored" Hexagon (SF Left, SS Right)
    # Strict Horizontal Mirror of "First Version"
    # Left (180): SF
    # Right (0): SS
    # Top-Left (120): DS (Mirrored from 60)
    # Top-Right (60): CF (Mirrored from 120)
    # Bottom-Left (240): CS (Mirrored from 300)
    # Bottom-Right (300): DF (Mirrored from 240)
    
    downstream_angles = {
        SAT.DownstreamSelectorSystem: 0,      # Right (Apex)
        SAT.DownstreamConsumerFunction: 60,   # Top-Right
        SAT.DownstreamDetectorSystem: 120,    # Top-Left
        SAT.DownstreamSelectorFunction: 180,  # Left (Apex)
        SAT.DownstreamConsumerSystem: 240,    # Bottom-Left
        SAT.DownstreamDetectorFunction: 300   # Bottom-Right
    }
    
    scale_downstream = 1.0 / np.sqrt(3)

    for cfg in configs:
        ax = cfg["ax"]
        mode = cfg["mode"] # UP, DOWN, BOTH
        vd = cfg["vd"]
        
        # We might loop twice if mode is BOTH
        layers_to_draw = []
        if mode == "UP" or mode == "BOTH":
            layers_to_draw.append("is_upstream")
        if mode == "DOWN" or mode == "BOTH":
            layers_to_draw.append("is_downstream")
            
        for layer in layers_to_draw:
            is_upstream = (layer == "is_upstream")
            
            node_positions = {}
            
            # 1. Calculate Positions
            if is_upstream:
                mapping = upstream_angles
                radius = 1.0
            else:
                mapping = downstream_angles
                radius = scale_downstream # Smaller
                
            for t, angle_deg in mapping.items():
                # Filter relevant types
                if ("Upstream" in t.name) != is_upstream: continue
                
                # Convert to radians
                angle_rad = np.radians(angle_deg)
                pos = (radius * np.cos(angle_rad), radius * np.sin(angle_rad))
                node_positions[t] = pos

            # 2. Draw Triangles (Background Geometry)
            # Only draw background polygons if we are not cluttering strict superposition, 
            # but usually it helps see the nesting.
            if is_upstream:
                # Functions Triangle
                ft_types = [SAT.UpstreamSelectorFunction, SAT.UpstreamConsumerFunction, SAT.UpstreamDetectorFunction]
                ft_pts = [node_positions[t] for t in ft_types if t in node_positions]
                if len(ft_pts) == 3:
                    poly = Polygon(ft_pts, closed=True, fill=True, facecolor='lightblue', alpha=0.1, edgecolor='blue', linestyle='--')
                    ax.add_patch(poly)
                    
                # Systems Triangle
                st_types = [SAT.UpstreamSelectorSystem, SAT.UpstreamConsumerSystem, SAT.UpstreamDetectorSystem]
                st_pts = [node_positions[t] for t in st_types if t in node_positions]
                if len(st_pts) == 3:
                    poly = Polygon(st_pts, closed=True, fill=True, facecolor='lightgreen', alpha=0.1, edgecolor='green', linestyle='--')
                    ax.add_patch(poly)
            else:
                # Downstream Inner Hexagon
                sorted_pts = sorted(node_positions.values(), key=lambda p: np.arctan2(p[1], p[0]))
                if len(sorted_pts) > 2:
                     poly = Polygon(sorted_pts, closed=True, fill=True, facecolor='orange', alpha=0.1, edgecolor='orange', linestyle=':')
                     ax.add_patch(poly)

            # 3. Draw Nodes and Labels
            for t, pos in node_positions.items():
                color = 'blue' if is_upstream else 'orange'
                if "System" in t.name: color = 'green' if is_upstream else 'brown'
                
                abbrev = "".join([c for c in t.name if c.isupper()]).replace("U", "").replace("D", "") # SF, SS
                
                # Draw Node
                ax.add_patch(plt.Circle(pos, 0.1, color=color, zorder=3))
                ax.text(pos[0], pos[1], abbrev, ha='center', va='center', fontweight='bold', color='white', fontsize=9, zorder=4)
                
                # Offset label
                label_offset = 1.2
                label_text = t.name.split("stream")[1]
                # If SUPERPOSITION, maybe suppress text or make smaller to avoid overlap?
                # For now let's keep it but slightly smaller if needed.
                font_sz = 6 if mode == "BOTH" else 7
                ax.text(pos[0]*label_offset, pos[1]*label_offset, label_text, ha='center', fontsize=font_sz)

            # 4. Draw Connections (Roads)
            for source_type, source_pos in node_positions.items():
                if is_upstream:
                    targets = get_upstream_connections(source_type, vd)
                else:
                    targets = get_downstream_connections(source_type, vd)
                    
                for target_type in targets:
                    if target_type in node_positions:
                        target_pos = node_positions[target_type]
                        ax.annotate("",
                                    xy=target_pos, xycoords='data',
                                    xytext=source_pos, textcoords='data',
                                    arrowprops=dict(arrowstyle="-|>", color="black", lw=2, mutation_scale=15),
                                    zorder=5) # High Z-order

        ax.set_title(cfg["title"])
        ax.set_aspect('equal')
        ax.axis('off')
        ax.set_xlim(-1.5, 1.5)
        ax.set_ylim(-1.5, 1.5)

    plt.tight_layout()
    plt.savefig('hexagon_schematics_viz.png', dpi=300)
    print("Visualization saved to hexagon_schematics_viz.png")

if __name__ == "__main__":
    visualize_schematics()

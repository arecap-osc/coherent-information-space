# informationalstream_py_app

Un mic "presenter" Python care reproduce fluxul din `InformationalStreamGraphPresenter`
din Java:

- generează toate cele patru rețele (Upstream/Downstream × Edge/Vertex) pentru
  o fereastră și `step`-urile dorite;
- combină rezultatele într-un singur obiect `GraphComposition` gata de desenare;
- oferă un script CLI (`python -m informationalstream_py_app.render_presenter`)
  care produce un PNG cu nodurile colorate pe netting și marcate diferit pe `step`.

## Cum rulezi

1. Instalează dependențele (dacă nu ai deja `matplotlib` și `numpy`):

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

2. Generează imaginea compusă (pași 1..4, distanță 600, ecran 1200×800):

```bash
python -m informationalstream_py_app.render_presenter \
  --width 1200 --height 800 --scale 1.0 --stream-distance 600 \
  --min-step 1 --max-step 4 --output informational_stream_composed.png
```

3. Dacă vrei să sari inferența de vecini (mai rapid, fără muchii):

```bash
python -m informationalstream_py_app.render_presenter --no-edges
```

## Structură

- `presenter.py` – clasele `ScreenProperties`, `PresenterNode(PresenterNodeKey)`,
  `GraphComposition` și `InformationalStreamGraphPresenterPy`. Apelează
  `InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy` pentru a genera
  nodurile și le etichetează pe `step/netting` la fel ca Java.
- `render_presenter.py` – CLI care compune pașii, desenează nodurile și opțional
  muchiile inferate (geometric) și scrie un PNG.

`ScreenProperties` folosește același sistem de coordonate ca Java: originea este
în centru, colțul din dreapta-jos este `(width/2, -height/2)`. Folosește
`--invert-y` dacă vrei să oglindești axa Y astfel încât originea să fie în
colțul stânga-sus în plot.

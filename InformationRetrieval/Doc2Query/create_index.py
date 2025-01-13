# ------------------------------------ SETUP ------------------------------------------------

from tira.third_party_integrations import ensure_pyterrier_is_loaded
from tira.rest_api_client import Client

ensure_pyterrier_is_loaded()
tira = Client()

from pyterrier import get_dataset, IterDictIndexer, BatchRetrieve
import pickle

# ------------------------------------ CODE ------------------------------------------------

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-deep-learning-20241201-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-deep-learning-20241201-training-inforet2024_25"

dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-rag-20250105-training"
attempt_name = "bm25-doc2query-subsampled-ms-marco-rag-20250105-training-inforet2024_25"

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-ir-lab-20250105-test"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-ir-lab-20250105-test-inforet2024_25"

pt_dataset = get_dataset(dataset)

meta = {
    'docno': 50,  # set this to at least 43 or higher to avoid truncation
    'text': 1000000  # adjusted value for text length
}

indexer = IterDictIndexer("./data/index", meta=meta)

index = indexer.index(pt_dataset.get_corpus_iter())

bm25 = BatchRetrieve(index, wmodel="BM25")

run = bm25(pt_dataset.get_topics('text'))

# save the 'run' object to a file
with open(f"/content/data/{attempt_name}.pkl", 'wb') as f:
    pickle.dump(run, f)

